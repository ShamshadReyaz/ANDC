package com.careerlauncher.gkninja.utils.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.careerlauncher.gkninja.R

class SoundService : Service() {
    var mp: MediaPlayer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mp = MediaPlayer.create(applicationContext, R.raw.gamesound)
        mp?.setLooping(true)
        super.onCreate()
    }

    override fun onDestroy() {
        mp!!.stop()
    }

    override fun onStart(intent: Intent, startid: Int) {
        mp!!.start()
    }
}