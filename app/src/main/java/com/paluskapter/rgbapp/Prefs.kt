package com.paluskapter.rgbapp

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefsFilename = "com.paluskapter.rgbapp.prefs"
    private val protocolValue = "protocol"
    private val hostValue = "host"
    private val portValue = "port"

    private val shared: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var protocol: String
        get() = shared.getString(protocolValue, "http")!!
        set(value) = shared.edit().putString(protocolValue, value).apply()

    var host: String
        get() = shared.getString(hostValue, "")!!
        set(value) = shared.edit().putString(hostValue, value).apply()

    var port: Int
        get() = shared.getInt(portValue, 0)
        set(value) = shared.edit().putInt(portValue, value).apply()
}
