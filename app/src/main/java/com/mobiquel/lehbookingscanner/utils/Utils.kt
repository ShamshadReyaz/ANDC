package com.mobiquel.lehbookingscanner.utils

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
