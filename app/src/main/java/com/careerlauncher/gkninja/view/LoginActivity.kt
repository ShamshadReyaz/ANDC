package com.careerlauncher.gkninja.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_sign_in.setOnClickListener {
            if (et_user_email.text.toString().equals("")) {
                showSnackBar("Please enter Email Address",btn_sign_in)
            } else if (et_password.text.toString().equals("")) {
                showSnackBar("Please enter Password",btn_sign_in)
            } else {

            }
        }
    }

    fun login(email:String,pwd:String){

    }

}