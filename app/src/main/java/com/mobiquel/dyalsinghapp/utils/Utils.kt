package com.mobiquel.dyalsinghapp.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Arman Reyaz on 7/1/2021.
 */

fun View.showSnackBar(message: String?, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message!!, Snackbar.LENGTH_SHORT)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Context.isValidEmail(input: String): Boolean {
    return input.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+".toRegex())
}

fun Context.validatePhoneNumber(phoneNo: String): Boolean {
    //validate phone numbers of format "1234567890"
    return if (phoneNo.matches("\\d{10}".toRegex())) true else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}".toRegex())) true else if (phoneNo.matches(
            "\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}".toRegex()
        )
    ) true else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}".toRegex())) true else false
}

