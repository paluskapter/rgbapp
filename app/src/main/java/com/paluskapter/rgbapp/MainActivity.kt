package com.paluskapter.rgbapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import java.net.URL
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), AnkoLogger {
    var prefs: Prefs? = null

    var protocol: String by Delegates.notNull()
    var host: String by Delegates.notNull()
    var port: Int by Delegates.notNull()

    var firstRun = true

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_color -> {
                colorLayout.visibility = View.VISIBLE
                modeLayout.visibility = View.INVISIBLE
                settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mode -> {
                colorLayout.visibility = View.INVISIBLE
                modeLayout.visibility = View.VISIBLE
                settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                colorLayout.visibility = View.INVISIBLE
                modeLayout.visibility = View.INVISIBLE
                settingsLayout.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = Prefs(this)
        protocol = prefs!!.protocol
        host = prefs!!.host
        port = prefs!!.port

        protocolEdit.setText(protocol)
        hostEdit.setText(host)
        portEdit.setText(port.toString())


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_mode

        rainbowButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/rainbow").readText()
            }
        }

        rainbowColorWipeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/rainbow_color_wipe").readText()
            }
        }

        rainbowFadeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/rainbow_fade").readText()
            }
        }

        voltageDropButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/voltage_drop").readText()
            }
        }

        randomFadeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/random_fade").readText()
            }
        }

        strobeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/strobe").readText()
            }
        }

        clearButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/clear").readText()
            }
        }

        colorPickerView.setColorListener { c ->
            if (!firstRun) {
                when {
                    both.isChecked -> {
                        doAsync {
                            URL(protocol, host, port, "/instant_color/${Color.red(c)}/${Color.green(c)}/${Color.blue(c)}").readText()
                        }
                        colorDisplay1.setBackgroundColor(c)
                        colorDisplay2.setBackgroundColor(c)
                    }

                    color1.isChecked -> {
                        val c2 = (colorDisplay2.background as ColorDrawable).color
                        doAsync {
                            URL(protocol, host, port, "/gradient/${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}/${Color.red(c)}/${Color.green(c)}/${Color.blue(c)}").readText()
                        }
                        colorDisplay1.setBackgroundColor(c)
                    }

                    color2.isChecked -> {
                        val c2 = (colorDisplay1.background as ColorDrawable).color
                        doAsync {
                            URL(protocol, host, port, "/gradient/${Color.red(c)}/${Color.green(c)}/${Color.blue(c)}/${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}").readText()
                        }
                        colorDisplay2.setBackgroundColor(c)
                    }
                }
            } else {
                firstRun = false
            }
        }

        both.setOnClickListener {
            if (both.isChecked) {
                color1.isEnabled = false
                color2.isEnabled = false
                colorgroup.clearCheck()
            } else {
                color1.isEnabled = true
                color2.isEnabled = true
                color1.isChecked = true
            }
        }

        colorDisplay1.setOnClickListener {
            if (!both.isChecked) {
                color1.isChecked = true
            }
        }

        colorDisplay2.setOnClickListener {
            if (!both.isChecked) {
                color2.isChecked = true
            }
        }

        protocolEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newText = protocolEdit.text.toString()
                prefs!!.protocol = newText
                protocol = newText
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        hostEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newText = hostEdit.text.toString()
                prefs!!.host = newText
                host = newText
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        portEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newText = portEdit.text.toString().toInt()
                prefs!!.port = newText
                port = newText
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }
}
