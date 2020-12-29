package com.careerlauncher.gkninja.view.login

import com.careerlauncher.gkninja.base.BaseModelListener
import com.careerlauncher.gkninja.pojo.LoginResponseModel
import com.careerlauncher.gkninja.pojo.RegistrationResponseModel
import com.careerlauncher.gkninja.pojo.SisAuthorizeResponseModel
import org.json.JSONObject

interface LoginModelListener : BaseModelListener {
    fun handleLoginResult(body: LoginResponseModel?)
    fun handleCreateAccountResult()
    fun handleSisAuthorizeResult(
        body: SisAuthorizeResponseModel?,
        uType: String?
    )

    fun handleSisAuthorizeResultFromParent(
        body: SisAuthorizeResponseModel?,
        uType: String?
    )

    fun handleHomePageURLResult(userType: String?)
    fun handleBannerURLsResponse()
    fun handleErrorResponse()
    fun handleBannerContentResponse()
    fun handleUserNamePwdResponse(jsonObject: JSONObject?, mode: String?)
    fun handleSignUpResult(registrationResponseModel: RegistrationResponseModel?)
}