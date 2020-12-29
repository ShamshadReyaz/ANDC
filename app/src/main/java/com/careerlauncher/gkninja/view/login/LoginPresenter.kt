package com.careerlauncher.gkninja.view.login

import android.util.Log
import com.careerlauncher.gkninja.base.BasePresenter
import com.careerlauncher.gkninja.pojo.LoginResponseModel
import com.careerlauncher.gkninja.pojo.RegistrationResponseModel
import com.careerlauncher.gkninja.pojo.SisAuthorizeResponseModel
import org.json.JSONObject
import java.util.*

class LoginPresenter(view: LoginView?) : BasePresenter<LoginView?>(view),
    LoginModelListener {
    private var model: LoginModel? = null
    override fun setModel() {
        model = LoginModel(this)
    }

    override fun destroy() {
        model?.detachListener()
        model = null
    }

    override fun initView() {}
    fun saveData(s1: String?, s2: String?, s3: String?) {
        getView()?.let {

        }
    }

    val datta: List<String?>
        get() {
            var data: List<String?> = ArrayList()
            // if (getView() != null) data = model!!.credential
            return data
        }


    fun getUserName(email: String?) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                //   model!!.getUsername(email)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun sisAuthrizeParam(
        prodCat: String?,
        userId: String?,
        regId: String?
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                //   model!!.sisAuthrizeParam(prodCat, userId, regId)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun onLoginClicked(email: String?, password: String?) {
        getView()?.let {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.login(email, password)
            } else {
                getView()!!.hideProgressBar()
                getView()!!.showNoNetworkError()
            }
        }.run {

        }

    }

    fun sisAuthrizeParam2(
        prodCat: String?,
        userId: String?,
        regId: String?
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                //   model!!.sisAuthrizeParam2(prodCat, userId, regId)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun verifyUserName(username: String?) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                //    model!!.verifyUsername(username)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getPassword(email: String?) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                // model!!.getPassword(email)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun onCreateAccountClicked() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                //  model!!.createAccount()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    override fun handleLoginResult(body: LoginResponseModel?) {
        Log.e("HANDLE LOGIN RSULT", " REACHED")
        if (getView() != null) {
            if (body != null) {
                if (body.loginStatus.equals("VALID")) {
                    if (body?.underDevelopment.equals("1") || body?.underMaintainance.equals("1")) {
                        if (body.underDevelopment.equals("1"))
                            getView()!!.showDevelopMaintain(1)
                        else
                            getView()!!.showDevelopMaintain(0)
                    } else {
                        model?.sisAuthrize()
                    }
                } else {
                    getView()!!.hideProgressBar()
                    getView()!!.showErrorMessage(body.loginStatus)
                }

            }
        } else {
            getView()!!.hideProgressBar()
            getView()!!.showErrorMessage("")
        }
    }

    override fun handleCreateAccountResult() {
        if (getView() != null) {
            getView()!!.showSelectUsertTypeScreen()
        }
    }

    override fun handleSisAuthorizeResult(body: SisAuthorizeResponseModel?, uType: String?) {
        if (getView() != null) {
            if (body != null) {
                if (body.status.equals("success")) {
                    getView()!!.hideProgressBar()
                    getView()!!.showHomeScreen()
                }
            } else {
                getView()!!.showErrorMessage("")
            }
        }
    }

    override fun handleSisAuthorizeResultFromParent(
        body: SisAuthorizeResponseModel?,
        uType: String?
    ) {
    }

    override fun handleHomePageURLResult(userType: String?) {

    }

    override fun handleBannerURLsResponse() {

    }

    override fun handleErrorResponse() {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            getView()!!.showHomeScreen()
        }
    }

    override fun handleBannerContentResponse() {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            getView()!!.showHomeScreen()
        }
    }

    override fun handleUserNamePwdResponse(
        jsonObject: JSONObject?,
        mode: String?
    ) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            getView()!!.showUserNamePwdResponse(jsonObject, mode)
        }
    }

    override fun handleSignUpResult(responseModel: RegistrationResponseModel?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()

        }
    }
}