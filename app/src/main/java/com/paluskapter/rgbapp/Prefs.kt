package com.paluskapter.rgbapp

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefsFilename = "com.paluskapter.rgbapp.prefs"
    private val protocolValue = "protocol"
    private val hostValue = "host"
    private val portValue = "port"

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var protocol: String
        get() = prefs.getString(protocolValue, "http")!!
        set(value) = prefs.edit().putString(protocolValue, value).apply()

    var host: String
        get() = prefs.getString(hostValue, "")!!
        set(value) = prefs.edit().putString(hostValue, value).apply()

    var port: Int
        get() = prefs.getInt(portValue, 0)
        set(value) = prefs.edit().putInt(portValue, value).apply()
}
