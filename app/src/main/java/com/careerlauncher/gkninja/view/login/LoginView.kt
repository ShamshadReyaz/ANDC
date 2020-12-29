package com.careerlauncher.gkninja.view.login

import com.careerlauncher.gkninja.base.BaseView
import com.careerlauncher.gkninja.pojo.RegistrationResponseModel
import org.json.JSONObject

interface LoginView : BaseView {
    val isValidated: Boolean
    fun showHomeScreen()
    fun showWebView()
    fun showSelectCourseActivty()
    fun showParentHomeScreen()
    fun showErrorMessage(status: String?)
    fun showSelectUsertTypeScreen()
    fun showDevelopMaintain(type: Int)
    fun showUserNamePwdResponse(jsonObject: JSONObject?, mode: String?)
    fun callSignUpIntent(
        userType: String?,
        responseModel: RegistrationResponseModel?
    )
}