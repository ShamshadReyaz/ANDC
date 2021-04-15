package com.mobiquel.lehpermitscanner.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobiquel.lehpermitscanner.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    val SPLASH_HOLD_TIME: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var context = this

        Handler(Looper.getMainLooper()).postDelayed({
                showHomeScreen()

        }, SPLASH_HOLD_TIME)

    }

    fun showHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}