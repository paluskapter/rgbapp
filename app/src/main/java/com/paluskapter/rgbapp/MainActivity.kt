package com.paluskapter.rgbapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.divyanshu.colorseekbar.ColorSeekBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import java.net.URL
import kotlin.properties.Delegates

//TODO: Readme update
//TODO: Fully material design

class MainActivity : AppCompatActivity(), AnkoLogger, ColorSeekBar.OnColorChangeListener {
    var prefs: Prefs? = null

    var protocol: String by Delegates.notNull()
    var host: String by Delegates.notNull()
    var port: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.selectedItemId = R.id.navigation_mode
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        prefs = Prefs(this)
        protocol = prefs!!.protocol
        host = prefs!!.host
        port = prefs!!.port

        protocolEdit.setText(protocol)
        hostEdit.setText(host)
        portEdit.setText(port.toString())

        colorSeekBar.setOnColorChangeListener(this)
        whiteSeekBar.setOnColorChangeListener(this)

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

        snakeColorButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/snake_color").readText()
            }
        }

        snakeFadeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/snake_fade").readText()
            }
        }

        snakeRainbowButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/snake_rainbow").readText()
            }
        }

        strobeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/strobe/300").readText()
            }
        }

        randomFadeButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/random_fade").readText()
            }
        }

        fireButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/fire").readText()
            }
        }

        voltageDropButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/voltage_drop").readText()
            }
        }

        musicButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/music").readText()
            }
        }

        clearButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/clear").readText()
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

    override fun onColorChangeListener(color: Int) {
        when {
            both.isChecked -> {
                doAsync {
                    URL(protocol, host, port, "/static_color/${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}").readText()
                }
                colorDisplay1.setBackgroundColor(color)
                colorDisplay2.setBackgroundColor(color)
            }

            color1.isChecked -> {
                val c2 = (colorDisplay2.background as ColorDrawable).color
                doAsync {
                    URL(protocol, host, port, "/static_gradient/${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}/${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}").readText()
                }
                colorDisplay1.setBackgroundColor(color)
            }

            color2.isChecked -> {
                val c2 = (colorDisplay1.background as ColorDrawable).color
                doAsync {
                    URL(protocol, host, port, "/static_gradient/${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}/${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}").readText()
                }
                colorDisplay2.setBackgroundColor(color)
            }
        }
    }

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
}
