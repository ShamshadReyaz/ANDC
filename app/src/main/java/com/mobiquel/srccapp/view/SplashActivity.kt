package com.mobiquel.srccapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.mobiquel.srccapp.databinding.ActivitySplashBinding
import com.mobiquel.srccapp.utils.Preferences

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                var status = "0";
                Preferences.instance!!.loadPreferences(this@SplashActivity)
                status = Preferences.instance!!.isLoginDone.toString()
                if (status == "0")
                    showLoginScreen()
                else
                    showHomeScreen()
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
        /*  Handler(Looper.getMainLooper()).postDelayed({
              var status = "0";
              Preferences.instance!!.loadPreferences(this)
              status = Preferences.instance!!.isLoginDone.toString()
              if (status == "0")
                  showLoginScreen()
              else
                  showHomeScreen()

          }, 1000)
  */
    }

    override fun onResume() {
        super.onResume()
        binding.motionLayout.startLayoutAnimation()

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