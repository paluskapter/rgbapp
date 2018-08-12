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
import android.widget.LinearLayout
import com.skydoves.colorpickerview.ColorListener
import org.jetbrains.anko.info


class MainActivity : AppCompatActivity(), AnkoLogger {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                colorLayout.visibility = View.VISIBLE
                modeLayout.visibility = View.INVISIBLE
                settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                colorLayout.visibility = View.INVISIBLE
                modeLayout.visibility = View.VISIBLE
                settingsLayout.visibility = View.INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
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

        colorButton.setOnClickListener {
            doAsync {
                resultBox.text = URL("http://192.168.0.8:5000/instant_color/${red.text}/${green.text}/${blue.text}").readText()
            }
        }

        rainbowButton.setOnClickListener {
            doAsync {
                resultBox.text = URL("http://192.168.0.8:5000/rainbow").readText()
            }
        }

        colorPickerView.setColorListener { color ->
            doAsync {
                resultBoxPicker.text = URL("http://192.168.0.8:5000/instant_color/${Color.red(color)}/${Color.green(color)}/${Color.blue(color)}").readText()
            }
            colorDisplay.setBackgroundColor(color)
        }
    }
}
