package com.mobiquel.lehpermitscanner

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.Volley

/**
 * Created by Arman Reyaz
 * on 12/4/19.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

    }


    companion object {
        @JvmStatic
        var instance: BaseApplication? = null
            private set
    }
}