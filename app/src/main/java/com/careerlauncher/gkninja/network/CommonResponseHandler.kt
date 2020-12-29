package com.careerlauncher.gkninja.network

/**
 * Created by Navjot Singh
 * on 2/3/19.
 * This is to be used for handling common responses
 * such as no network
 */
interface CommonResponseHandler {
    fun onNetworkError()
}