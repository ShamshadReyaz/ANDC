package com.careerlauncher.gkninja.base

import com.careerlauncher.gkninja.pojo.FailureResponse

/**
 * Created by Navjot Singh
 * on 2/3/19.
 */
interface BaseModelListener {
    fun noNetworkError()
    fun onErrorOccurred(failureResponse: FailureResponse?)
}