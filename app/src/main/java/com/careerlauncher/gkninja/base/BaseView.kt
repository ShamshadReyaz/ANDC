package com.careerlauncher.gkninja.base

import android.view.View
import com.careerlauncher.gkninja.pojo.FailureResponse

/**
 * Created by Navjot Singh
 * on 2/3/19
 */
interface BaseView {
    fun showNoNetworkError()
    fun showProgressBar()
    fun hideProgressBar()
    val isNetworkAvailable: Boolean
    fun showSnackBar(message: String?,view: View?=null)
    fun showSpecificError(failureResponse:FailureResponse?)
}