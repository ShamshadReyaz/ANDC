package com.mobiquel.srccapp

import android.app.Application
import com.mobiquel.srccapp.appinterface.AppComponent

/**
 * Created by Arman Reyaz
 * on 12/4/19.
 */
class BaseApplication : Application() {

    lateinit var appComponent: AppComponent
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