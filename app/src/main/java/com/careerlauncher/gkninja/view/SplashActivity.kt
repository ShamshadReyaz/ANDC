package com.careerlauncher.gkninja.view

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import com.careerlauncher.gkninja.R

class SplashActivity: AppCompatActivity() {

    val SPLASH_HOLD_TIME=4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val custom="Powered by "
        val spanTxt=SpannableStringBuilder(custom+" ")
        spanTxt.append("CareerLauncher")
        //spanTxt.inSpans(ClickableSpan)

    }
}