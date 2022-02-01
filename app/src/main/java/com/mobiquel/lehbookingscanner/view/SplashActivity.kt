package com.mobiquel.lehbookingscanner.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.mobiquel.lehbookingscanner.R
import com.mobiquel.lehbookingscanner.data.DataStoreManager
import com.mobiquel.lehbookingscanner.utils.Preferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    val SPLASH_HOLD_TIME: Long = 1000
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        dataStoreManager = DataStoreManager(this@SplashActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            var status="0";
            Preferences.instance!!.loadPreferences(this)
            status= Preferences.instance!!.isLoginDone.toString()
            if(status=="0")
                showLoginScreen()
            else
                showHomeScreen()


        }, SPLASH_HOLD_TIME)

    }

    fun showHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun showLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}