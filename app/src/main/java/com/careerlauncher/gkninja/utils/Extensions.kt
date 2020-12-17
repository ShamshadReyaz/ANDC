package com.careerlauncher.gkninja.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.careerlauncher.gkninja.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Arman Reyaz on 12/18/2020.
 */
fun Context.showSnackBar(message: String, view: View) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
    val textView =
        snackbar.view.findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
    snackbar.show()
}