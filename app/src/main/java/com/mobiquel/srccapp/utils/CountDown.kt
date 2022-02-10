package com.mobiquel.srccapp.utils

import android.os.CountDownTimer

/**
 * Created by Arman Reyaz on 12/24/2020.
 */
 class CountDown(millisInFuture: Long, countDownInterval: Long) :CountDownTimer(millisInFuture, countDownInterval) {

    override fun onFinish() {
    }

    override fun onTick(millisUntilFinished: Long) {
    }
}