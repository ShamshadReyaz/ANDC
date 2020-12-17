package com.careerlauncher.gkninja.data

import android.content.Context
import com.careerlauncher.gkninja.data.preferences.PreferenceManager

/**
 * Created by Arman Reyaz
 * on 12/17/2020.
 */
class DataManager(context: Context) {
    private val preferenceManager: PreferenceManager? = PreferenceManager.init(context)

    /**
     * method to save value in preferences
     */

    fun saveStringInPreference(key: String?, value: String?) {
        preferenceManager!!.setString(key, value)
    }

    /**
     * method to save value in preferences
     */
    fun saveIntInPreference(key: String?, value: Int) {
        preferenceManager!!.setInt(key, value)
    }

    /**
     * method to save value in preferences
     */
    fun saveBooleanInPreference(key: String?, value: Boolean) {
        preferenceManager!!.setBoolean(key, value)
    }

    /**
     * method to get value from preferences
     */
    fun getStringFromPreference(key: String?): String? {
        return preferenceManager!!.getString(key)
    }

    /**
     * method to get value from preferences
     */
    fun getIntFromPreference(key: String?): Int {
        return preferenceManager!!.getInt(key)
    }

    /**
     * method to get value from preferences
     */
    fun getBooleanFromPreference(key: String?): Boolean {
        return preferenceManager!!.getBoolean(key)
    } // API

    companion object {
        var instance: DataManager? = null
            private set

        @Synchronized
        fun init(context: Context) {
            if (instance == null) {
                instance = DataManager(context)
            }
        }
    }
}