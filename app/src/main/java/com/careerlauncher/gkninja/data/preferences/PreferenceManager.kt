package com.careerlauncher.gkninja.data.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Arman Reyaz
 * on 12/17/2020.
 */
class PreferenceManager private constructor(private val context: Context) {
    private var preference: SharedPreferences? = null
    private fun initPreference() {
        if (preference == null) {
            preference = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    fun getString(key: String?): String? {
        return preference!!.getString(key, "")
    }

    fun setString(key: String?, value: String?) {
        val editor = preference!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getInt(key: String?): Int {
        return preference!!.getInt(key, 0)
    }

    fun setInt(key: String?, value: Int) {
        val editor = preference!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getBoolean(key: String?): Boolean {
        return preference!!.getBoolean(key, false)
    }

    fun setBoolean(key: String?, value: Boolean) {
        val editor = preference!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun clearAllPrefs() {
        val editor = preference!!.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        var instance: PreferenceManager? = null
            private set

        fun init(context: Context): PreferenceManager? {
            if (instance == null) {
                synchronized(
                    PreferenceManager::class.java
                ) {
                    if (instance == null) instance =
                        PreferenceManager(
                            context
                        )
                }
            }
            return instance
        }

    }

    init {
        initPreference()
    }
}