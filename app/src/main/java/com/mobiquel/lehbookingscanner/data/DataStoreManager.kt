package com.mobiquel.lehbookingscanner.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Arman Reyaz on 10/22/2021.
 */
const val USER_DATASTORE = "datastore"

class DataStoreManager(val context: Context) {

   // private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

    private val dataStore = context.createDataStore(name = "user_prefs")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val USER_MOBILE_KEY = preferencesKey<String>("USER_MOBILE")
        val USER_ID_KEY = preferencesKey<String>("USER_ID")
        val USER_NAME_KEY = preferencesKey<String>("USER_NAME")
        val USER_LOGGED_IN_KEY = preferencesKey<Boolean>("USER_LOGGED_IN")

    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeUser(name: String, mobile: String,id: String, loggedIn: Boolean) {
        Log.e("DATA====","STORE===== "+loggedIn)

        dataStore.edit {
            it[USER_MOBILE_KEY] = mobile
            it[USER_NAME_KEY] = name
            it[USER_ID_KEY] = id
            it[USER_LOGGED_IN_KEY] = loggedIn

        }
    }
    val userLoginStats: Flow<Boolean> = dataStore.data.map {
        it[USER_LOGGED_IN_KEY] ?: false
    }

}