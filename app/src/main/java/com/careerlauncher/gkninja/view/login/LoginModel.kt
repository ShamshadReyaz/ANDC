package com.careerlauncher.gkninja.view.login

import android.util.Log
import com.careerlauncher.gkninja.base.BaseModel
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.network.NetworkResponse
import com.careerlauncher.gkninja.pojo.*

class LoginModel(listener: LoginModelListener?) : BaseModel<LoginModelListener?>(listener) {
    private var prodDirect = ""
    private var prodCat = ""
    private val prodId = ""
    override fun init() {}

    fun login(email: String?, pwd: String?) {
        val loginRequest = LoginRequest()
        loginRequest.username = email
        loginRequest.password = pwd
        loginRequest.rememberMe = "true"
        loginRequest.latitude = "0.0"
        loginRequest.longitude = "0.0"

        dataManager.login(loginRequest)
            ?.enqueue(object : NetworkResponse<LoginResponseModel?>(this) {
                override fun onSuccess(body: LoginResponseModel?) {
                    body?.let {
                        if (body.loginStatus.equals("VALID")) {
                            Log.e("RESPONSE", "" + body.loginStatus)
                            if (body.userType!!.contains("PARENT"))
                                dataManager.saveStringInPreference(PrefKeys.PARENT_ID, body.userId)
                            else
                                dataManager.saveStringInPreference(PrefKeys.USER_ID, body.userId)

                            if (body.webUrl == "YES")
                                dataManager.saveStringInPreference(PrefKeys.IS_WEBVIEW_ALLOWED, "1")
                            else
                                dataManager.saveStringInPreference(PrefKeys.IS_WEBVIEW_ALLOWED, "0")

                            dataManager.saveStringInPreference(PrefKeys.BASE_URL, body.baseUrl)
                            dataManager.saveStringInPreference(PrefKeys.USER_NAME, email)
                            dataManager.saveStringInPreference(PrefKeys.USER_PASSWORD, pwd)
                            dataManager.saveStringInPreference(PrefKeys.USER_TYPE, body.userType)
                            dataManager.saveStringInPreference(PrefKeys.PROD_CAT, body.prodCat)
                            dataManager.saveStringInPreference(PrefKeys.PROD_CAT_FIX, body.prodCat)

                            prodDirect = body.directory.toString()
                            prodCat = body.prodCat.toString()
                            dataManager.saveStringInPreference(PrefKeys.PROD_DIRECT, body.directory)
                            dataManager.saveStringInPreference(
                                PrefKeys.PROD_DIRECT_FIX,
                                body.directory
                            )
                            NetworkConstants.changeApiBaseUrl(body.baseUrl.toString())
                        }
                        listener!!.get()?.handleLoginResult(body)
                    }
                }

                override fun onFailure(code: Int, failureResponse: FailureResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onError(t: Throwable?) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun sisAuthrize() {
        val sisRequest = SisAuthorizeRequest()
        sisRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        sisRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        sisRequest.isLoggedIn = "true"

        dataManager.sisAuthorize(sisRequest)
            ?.enqueue(object : NetworkResponse<SisAuthorizeResponseModel?>(this) {
                override fun onSuccess(body: SisAuthorizeResponseModel?) {
                    body?.let {
                        if (body.status == "success") {
                            dataManager.saveStringInPreference(PrefKeys.PROD_ID, body.prodId)
                            dataManager.saveStringInPreference(PrefKeys.PROD_ID_FIX, body.prodId)
                            dataManager.saveStringInPreference(PrefKeys.TEST_GYM, body.testGym)
                            dataManager.saveStringInPreference(PrefKeys.SERVICE_LOC, body.serviceLocation)
                            dataManager.saveStringInPreference(PrefKeys.DEMO_FLAG, body.demoFlag)
                            dataManager.saveStringInPreference(PrefKeys.PROD_TYPE, body.prodType)
                            dataManager.saveStringInPreference(PrefKeys.EXTERNAL_URL_CHECK, body.prodType)
                        } else {
                            dataManager.saveBooleanInPreference(PrefKeys.IS_LOGGED_IN, false)
                        }
                        listener!!.get()?.handleSisAuthorizeResult(body,dataManager.getStringFromPreference(PrefKeys.USER_TYPE))

                    }
                }

                override fun onFailure(code: Int, failureResponse: FailureResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onError(t: Throwable?) {
                    TODO("Not yet implemented")
                }

            })
    }
}