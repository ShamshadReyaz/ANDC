package com.careerlauncher.gkninja.view.login

import android.content.Intent
import android.os.Bundle
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseActivity
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.pojo.FailureResponse
import com.careerlauncher.gkninja.pojo.RegistrationResponseModel
import com.careerlauncher.gkninja.view.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity(), LoginView {

    var mPresenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPresenter = LoginPresenter(this)

        et_user_email.setText("clcat2016".toString())
        et_password.setText("aspiration2020".toString())

        btn_sign_in.setOnClickListener {
            if (et_user_email.text.toString().equals("")) {
                showSnackBar("Please enter Username", btn_sign_in)
            } /*else if (!isValidEmail(et_user_email.text.toString())) {
                showSnackBar("Please enter valid emailId", btn_sign_in)
            } */ else if (et_password.text.toString().equals("")) {
                showSnackBar("Please enter Password", btn_sign_in)
            } else {
                //   showProgressBar()
                login(et_user_email.text.toString(), et_password.text.toString())
            }
        }
    }

    override fun initVariables() {
        TODO("Not yet implemented")
    }

    override fun setListeners() {
        TODO("Not yet implemented")
    }

    override val resourceId: Int
        get() = R.layout.activity_login
    override val isValidated: Boolean
        get() = checkValidation()

    fun checkValidation(): Boolean {
        return if (et_user_email.getText().toString().trim({ it <= ' ' }).length == 0) {
            //showSnackBar(getString(R.string.msg_enter_email));
            showSnackBar(getString(R.string.msg_enter_email), btn_sign_in)
            false
        } else if (et_password.getText().toString().trim({ it <= ' ' }).length == 0) {
            //showSnackBar(getString(R.string.msg_enter_password));
            showSnackBar(getString(R.string.msg_enter_email), btn_sign_in)
            false
        } else {
            true
        }
    }

    override fun showHomeScreen() {
        //showSnackBar("Congrats! LoggedIn successfully", btn_sign_in)
        val dmgr=DataManager(this)
        dmgr.saveBooleanInPreference(PrefKeys.IS_LOGGED_IN,true)
        startActivity(Intent(this,
            HomeActivity::class.java))
        finish()

    }

    override fun showWebView() {
        TODO("Not yet implemented")
    }

    override fun showSelectCourseActivty() {
        TODO("Not yet implemented")
    }

    override fun showParentHomeScreen() {
        TODO("Not yet implemented")
    }

    override fun showErrorMessage(status: String?) {

        if (status.equals("INVALID"))
            showSnackBar("Please enter correct cerdentials!", btn_sign_in)
        else
            showSnackBar(status, btn_sign_in)
    }

    override fun showSelectUsertTypeScreen() {
        TODO("Not yet implemented")
    }

    override fun showDevelopMaintain(type: Int) {
        TODO("Not yet implemented")
    }

    override fun showUserNamePwdResponse(jsonObject: JSONObject?, mode: String?) {
        TODO("Not yet implemented")
    }

    override fun callSignUpIntent(userType: String?, responseModel: RegistrationResponseModel?) {
        TODO("Not yet implemented")
    }

    override fun showSpecificError(failureResponse: FailureResponse?) {
    }

    fun login(email: String, pwd: String) {
        mPresenter?.onLoginClicked(email, pwd)
    }

}