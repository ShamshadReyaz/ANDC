package com.careerlauncher.gkninja

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.Volley
import com.careerlauncher.gkninja.data.DataManager

/**
 * Created by Arman Reyaz
 * on 12/4/19.
 */
class BaseApplication : Application() {
    private var mRequestQueue: RequestQueue? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        DataManager.init(instance!!)

    }

    // lazy initialize the request queue, the queue instance will be
    // created when it is accessed for the first time
    val requestQueue: RequestQueue?
        get() {
            // lazy initialize the request queue, the queue instance will be
            // created when it is accessed for the first time
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }
            return mRequestQueue
        }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) VolleyLog.TAG else tag
        VolleyLog.d("Adding request to queue: %s", req.url)
        requestQueue!!.add(req)
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param
     * @param
     */
    fun <T> addToRequestQueue(req: Request<T>) {
        // set the default tag if tag is empty
        req.tag = VolleyLog.TAG
        requestQueue!!.add(req)
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    fun cancelPendingRequests(tag: Any?) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        @JvmStatic
        var instance: BaseApplication? = null
            private set
    }
}