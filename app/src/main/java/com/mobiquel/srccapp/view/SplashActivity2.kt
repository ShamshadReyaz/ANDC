package com.mobiquel.srccapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobiquel.srccapp.databinding.ActivitySplash2Binding
import com.mobiquel.srccapp.utils.Preferences

class SplashActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivitySplash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            var status = "0";
            Preferences.instance!!.loadPreferences(this)
            status = Preferences.instance!!.isLoginDone.toString()
            if (status == "0")
                showLoginScreen()
            else
                showHomeScreen()

        }, 1000)

    }

    private fun showHomeScreen() {
            Preferences.instance!!.loadPreferences(this@SplashActivity2)
            Log.e("PREF",Preferences.instance?.userType!!)
            if (Preferences.instance?.userType.equals("faculty") || Preferences.instance?.userType.equals("non-teaching")) {
                startActivity(Intent(this, FacultyHomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
    }

    private fun showLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}