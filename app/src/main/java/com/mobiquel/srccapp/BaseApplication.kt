package com.mobiquel.srccapp

import android.app.Application

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