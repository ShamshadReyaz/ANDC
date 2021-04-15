package com.mobiquel.lehpermitscanner.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

/**
 * Created by Arman Reyaz on 12/18/2020.
 */

/*
fun Context.showSnackBar(message: String, view: View) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
    val textView =
        snackbar.view.findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
    snackbar.show()
}
*/


fun Context.isValidEmail(input: String): Boolean {
    return input.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+".toRegex())
}
fun Context.showToast(input: String)  {
    Toast.makeText(this,input,Toast.LENGTH_SHORT).show()
}

fun Context.getAppVersion(): String {
    var version = ""
    try {
        val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return version
}

fun Context.setImageGlide(view: ImageView, url: String)  {
    Glide.with(this)
        .load(url)
        .into(view)

}
fun Context.setImageGlide(view: ImageView, url: Int)  {
    Glide.with(this)
        .load(url)
        .into(view)

}


fun Context.getDeviceId(): String {
    var deviceId = ""
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
        deviceId=Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID)
    }
    else
    {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= 23) {
            val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
            val activity=this as Activity
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
            }
            else{
                telephonyManager.deviceId?.let {
                    deviceId=telephonyManager.deviceId
                }.run {
                    deviceId=telephonyManager.deviceId
                }

            }
        }
        else
        {
            telephonyManager.deviceId?.let {
                deviceId=telephonyManager.deviceId
            }.run {
                deviceId=telephonyManager.deviceId
            }

        }
    }
    return deviceId
}


