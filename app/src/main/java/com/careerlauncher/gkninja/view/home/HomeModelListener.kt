package com.careerlauncher.gkninja.view.home

import com.careerlauncher.gkninja.base.BaseModelListener
import com.careerlauncher.gkninja.pojo.LogoutResponseModel
import org.json.JSONObject

interface HomeModelListener : BaseModelListener {
    fun handleLogoutResult(body: LogoutResponseModel?)
    fun handleBannerURLsResponse(jsonObject: JSONObject?)
    fun handleErrorResponse()
    fun handleSubmiResponse(jsonObject: JSONObject?)
    fun handleRegisterAppResponse(jsonObject: JSONObject?)
    fun handleGetAppVersionResponse(jsonObject: JSONObject?)
    fun handleRankResponse(jsonObject: JSONObject?)
    fun handleQuizResponse(jsonObject: JSONObject?)
    fun handleTermsConditionResponse(jsonObject: JSONObject?)
    fun handleProfileDataResponse(jsonObject: JSONObject?)

}