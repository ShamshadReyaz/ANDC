package com.careerlauncher.gkninja.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.view.home.HomeActivity
import com.careerlauncher.gkninja.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    val SPLASH_HOLD_TIME: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val custom = "Powered by CarrerLauncher.com".toSpannable()
        var context = this
        custom[11..24] = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(context, "Clicked: Lorem", Toast.LENGTH_LONG).show()
            }
        }
        footer.movementMethod = LinkMovementMethod.getInstance()
        footer.text = custom
        lineTextView.animateText("CL GK Ninja");
        fadeTextView.animateText("From Career Launcher");

        /*Handler(Looper.getMainLooper()).postDelayed({
            showHomeScreen()
        },SPLASH_HOLD_TIME)
        */
        Handler(Looper.getMainLooper()).postDelayed({

            if (DataManager(context).getBooleanFromPreference(PrefKeys.IS_LOGGED_IN))
                showHomeScreen()
            else
                showLoginScreen()

            finish()

        }, SPLASH_HOLD_TIME)

    }

    fun showHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun showLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}