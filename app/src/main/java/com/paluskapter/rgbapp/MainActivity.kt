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
import com.paluskapter.rgbapp.databinding.ActivityMainBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), AnkoLogger, ColorSeekBar.OnColorChangeListener {
    private var prefs: Prefs? = null

    private var protocol: String by Delegates.notNull()
    private var host: String by Delegates.notNull()
    private var port: Int by Delegates.notNull()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.navigation.selectedItemId = R.id.navigation_mode

        prefs = Prefs(this)
        protocol = prefs!!.protocol
        host = prefs!!.host
        port = prefs!!.port

        configureUserInterface()
    }

    private fun configureUserInterface() {
        binding.protocolEdit.setText(protocol)
        binding.hostEdit.setText(host)
        binding.portEdit.setText(port.toString())

        binding.colorSeekBar.setOnColorChangeListener(this)
        binding.whiteSeekBar.setOnColorChangeListener(this)

        binding.colorDisplay1.setOnClickListener {
            if (!binding.both.isChecked) {
                binding.color1.isChecked = true
            }
        }

        binding.colorDisplay2.setOnClickListener {
            if (!binding.both.isChecked) {
                binding.color2.isChecked = true
            }
        }

        binding.both.setOnClickListener {
            if (binding.both.isChecked) {
                binding.color1.isEnabled = false
                binding.color2.isEnabled = false
                binding.colorgroup.clearCheck()
            } else {
                binding.color1.isEnabled = true
                binding.color2.isEnabled = true
                binding.color1.isChecked = true
            }
        }

        binding.rainbowButton.setOnClickListener { sendRequest("/rainbow") }
        binding.rainbowColorWipeButton.setOnClickListener { sendRequest("/rainbow_color_wipe") }
        binding.rainbowFadeButton.setOnClickListener { sendRequest("/rainbow_fade") }
        binding.strobeButton.setOnClickListener { sendRequest("/strobe") }
        binding.randomFadeButton.setOnClickListener { sendRequest("/random_fade") }
        binding.snakeColorButton.setOnClickListener { sendRequest("/snake_color") }
        binding.snakeFadeButton.setOnClickListener { sendRequest("/snake_fade") }
        binding.snakeRainbowButton.setOnClickListener { sendRequest("/snake_rainbow") }
        binding.fireButton.setOnClickListener { sendRequest("/fire") }
        binding.clearButton.setOnClickListener { sendRequest("/clear") }

        binding.protocolEdit.addTextChangedListener(createTextWatcher {
            val newText = binding.protocolEdit.text.toString()
            prefs!!.protocol = newText
            protocol = newText
        })

        binding.hostEdit.addTextChangedListener(createTextWatcher {
            val newText = binding.hostEdit.text.toString()
            prefs!!.host = newText
            host = newText
        })

        binding.portEdit.addTextChangedListener(createTextWatcher {
            val newText = binding.portEdit.text.toString().toInt()
            prefs!!.port = newText
            port = newText
        })
    }

    override fun onColorChangeListener(color: Int) {
        when {
            binding.both.isChecked -> {
                doAsync {
                    val endpoint = "/static_color/" +
                            "${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}"
                    URL(protocol, host, port, endpoint).readText()
                }
                binding.colorDisplay1.setBackgroundColor(color)
                binding.colorDisplay2.setBackgroundColor(color)
            }

            binding.color1.isChecked -> {
                val c2 = (binding.colorDisplay2.background as ColorDrawable).color
                val endpoint = "/static_gradient/" +
                        "${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}/" +
                        "${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}"
                doAsync {
                    URL(protocol, host, port, endpoint).readText()
                }
                binding.colorDisplay1.setBackgroundColor(color)
            }

            binding.color2.isChecked -> {
                val c2 = (binding.colorDisplay1.background as ColorDrawable).color
                val endpoint = "/static_gradient/" +
                        "${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}/" +
                        "${Color.red(c2)}/${Color.green(c2)}/${Color.blue(c2)}"
                doAsync {
                    URL(protocol, host, port, endpoint).readText()
                }
                binding.colorDisplay2.setBackgroundColor(color)
            }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_color -> {
                binding.colorLayout.visibility = View.VISIBLE
                binding.modeLayout.visibility = View.INVISIBLE
                binding.settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mode -> {
                binding.colorLayout.visibility = View.INVISIBLE
                binding.modeLayout.visibility = View.VISIBLE
                binding.settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                binding.colorLayout.visibility = View.INVISIBLE
                binding.modeLayout.visibility = View.INVISIBLE
                binding.settingsLayout.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun sendRequest(url: String) {
        doAsync {
            URL(protocol, host, port, url).readText()
        }
    }

    private fun createTextWatcher(onChangeFunc: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no-op
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onChangeFunc()
            }

            override fun afterTextChanged(p0: Editable?) {
                // no-op
            }
        }
    }
}
