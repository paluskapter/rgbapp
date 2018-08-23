package com.paluskapter.rgbapp

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val PREFS_FILENAME = "com.paluskapter.rgbapp.prefs"
    val PROTOCOL = "protocol"
    val HOST = "host"
    val PORT = "port"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var protocol: String
        get() = prefs.getString(PROTOCOL, "http")!!
        set(value) = prefs.edit().putString(PROTOCOL, value).apply()

    var host: String
        get() = prefs.getString(HOST, "")!!
        set(value) = prefs.edit().putString(HOST, value).apply()

    var port: Int
        get() = prefs.getInt(PORT, 0)
        set(value) = prefs.edit().putInt(PORT, value).apply()
}
