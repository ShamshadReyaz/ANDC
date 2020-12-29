package com.careerlauncher.gkninja.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.utils.AppConstants
import com.careerlauncher.gkninja.utils.showToast
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Arman Reyaz on 12/18/2020.
 */

open abstract class BaseActivity : AppCompatActivity(), BaseView {

    var frameLayout: FrameLayout? = null
    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        frameLayout = findViewById(R.id.fl_base_container)
        initVariables()
        setListeners()
        setLayout()
    }

    override fun showNoNetworkError() {
        //showSnackBar(AppConstants.MSG_NETWORK_ERROR)
        showToast(AppConstants.MSG_NETWORK_ERROR)
    }

    private fun setLayout() {
        if (resourceId != -1) {
            removeLayout()
            layoutInflater.inflate(resourceId, frameLayout, true)
        }
    }
    protected abstract fun initVariables()
    protected abstract fun setListeners()

    private fun removeLayout() {
        if (frameLayout != null && frameLayout!!.childCount >= 1) frameLayout!!.removeAllViews()
    }

    protected abstract val resourceId: Int

    override val isNetworkAvailable: Boolean
        get() = isNetWOrkAvailable()

    fun isNetWOrkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }

        return false
    }
    override fun showProgressBar() {
        try {
            if (!isFinishing) {
                hideProgressBar()
                mProgressDialog = Dialog(this)
                mProgressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val view = LayoutInflater.from(this).inflate(R.layout.layout_progressbar, null)
                mProgressDialog!!.setContentView(view)
                view.visibility = View.VISIBLE
                mProgressDialog!!.setCancelable(true)
                if (mProgressDialog!!.window != null) {
                    mProgressDialog!!.window!!.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            this,
                            android.R.color.transparent
                        )
                    )
                    mProgressDialog!!.window!!.setDimAmount(0f)
                    mProgressDialog!!.window!!.setGravity(Gravity.CENTER)
                }
                mProgressDialog!!.show()
            }
        } catch (e: IllegalArgumentException) {
        }
    }

    override fun hideProgressBar() {
        try {
            if (!isFinishing && mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
                mProgressDialog = null
            }
        } catch (e: IllegalArgumentException) {
        }
    }



    override fun showSnackBar(message: String?, view: View?) {
        view?.let {
            val snackbar = Snackbar.make(view, message.toString(), Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            val textView =
                snackbar.view.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            snackbar.show()
        } ?: run {
            val snackbar = Snackbar.make(frameLayout!!, message.toString(), Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            val textView =
                snackbar.view.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            snackbar.show()

        }

    }
}