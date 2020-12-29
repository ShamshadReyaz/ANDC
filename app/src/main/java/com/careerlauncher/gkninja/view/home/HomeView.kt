package com.careerlauncher.gkninja.view.home

import com.careerlauncher.gkninja.base.BaseView
import org.json.JSONObject

interface HomeView : BaseView {
    fun setUpBottomNavigationView()
    fun showLoginSignUp()
    fun showErrorMessage()
    fun registerAppSubmit(jsonObject: JSONObject?)
    fun getAppVersion(jsonObject: JSONObject?)
    fun handleDashboard()
    fun setUpRankData(jsonObject: JSONObject?)
    fun setUpGameQuestion(jsonObject: JSONObject?)
    fun setUpData(jsonObject: JSONObject?)

}