package com.mobiquel.dyalsinghapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Arman Reyaz
 * on 12/4/19.
 */
@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        //appComponent=DaggerCo

    }


    companion object {
        @JvmStatic
        var instance: BaseApplication? = null
            private set
    }
}