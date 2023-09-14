package com.mobiquel.srccapp.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mobiquel.srccapp.R

/**
 * Created by Arman Reyaz on 12/18/2020.
 */


fun Context.showSnackBar(message: String, view: View) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
    val textView =
        snackbar.view.findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
    textView.textSize=14f
    snackbar.show()
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
   /* Glide.with(this)
        .load(url)
        .into(view)
*/
}
fun Context.setImageGlide(view: ImageView, url: Int)  {
   /* Glide.with(this)
        .load(url)
        .into(view)
*/
}

fun Context.redirectToWeb2(link: String) {
    try{
        val uri =
            Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }catch (e:Exception){
        showToast("invalid URL")
    }

}
fun Context.getDeviceId(): String {
    var deviceId = ""
  /*  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
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
  */
    return deviceId
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.e("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.e("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.e("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
fun Context.showImagePopup(imageurl:String) {

    var dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_webview)
    dialog.window!!.setDimAmount(0.5f)
    dialog.window!!.setBackgroundDrawable(null)
    dialog.window!!.getAttributes().windowAnimations = R.style.DialogBounceAnimation
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    dialog.window!!.setGravity(Gravity.CENTER)
    val cancel = dialog.findViewById<ImageView>(R.id.cancel)
    val webview = dialog.findViewById<WebView>(R.id.webview)
    webview.settings.builtInZoomControls=true
    webview.settings.displayZoomControls=false
    webview.settings.useWideViewPort=true
    webview.setInitialScale(1)
    webview.loadUrl(imageurl)
    cancel.setOnClickListener {
        dialog.cancel()
    }

    dialog.show()
}

fun Context.showSingleButtonDialog(message:String){
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialogInterface, which ->
        dialogInterface.cancel()
    }
    val alertDialog = builder.create()
    alertDialog.setCancelable(true)
    alertDialog.show()
}


