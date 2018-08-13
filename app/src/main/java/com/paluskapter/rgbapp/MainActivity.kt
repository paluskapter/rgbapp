package com.paluskapter.rgbapp

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import java.net.URL


class MainActivity : AppCompatActivity(), AnkoLogger {
    val protocol = "http"
    val host = "192.168.0.8"
    val port = 5000

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

        clearButton.setOnClickListener {
            doAsync {
                URL(protocol, host, port, "/clear").readText()
            }
        }

        colorPickerView.setColorListener { color ->
            doAsync {
                URL(protocol, host, port, "/instant_color/${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}").readText()
            }
            colorDisplay.setBackgroundColor(color)
        }
    }
}
