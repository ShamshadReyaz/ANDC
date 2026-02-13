package com.mobiquel.dyalsinghapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiquel.dyalsinghapp.R
import com.mobiquel.dyalsinghapp.databinding.ActivityLoginBinding
import com.mobiquel.dyalsinghapp.data.ApiManager
import com.mobiquel.dyalsinghapp.utils.Preferences
import com.mobiquel.dyalsinghapp.utils.showSnackBar
import com.mobiquel.dyalsinghapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    var context: Context? = null

    @Inject
    lateinit var apiManager: ApiManager
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this@LoginActivity
        binding.img.bringToFront()
        binding.btnSignIn.setOnClickListener {
            if (binding.userTypeGrp.checkedRadioButtonId == -1)
                showSnackBar("Please select user type!", binding.rlMain)
            else if (binding.username.text.toString().equals(""))
                showSnackBar("Please enter emailId!", binding.rlMain)
            else if (binding.password.text.toString().equals(""))
                showSnackBar("Please enter Password!", binding.rlMain)
            else {
                login()
            }

        }

       binding.userTypeGrp.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup, p1: Int) {
                val userType =
                    resources.getResourceEntryName(p1) // "male"
                if (userType.equals("faculty")) {
                    binding.usernameLayout.setHint("Enter Username")
                }
                if (userType.equals("student")) {
                    binding.usernameLayout.setHint("Enter EmailId")
                }
            }
        })

   binding.faculty.isChecked = true




    }

    private fun login() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        val genderUserType =
            resources.getResourceEntryName(binding.userTypeGrp.checkedRadioButtonId) // "male"

        data["email"] = binding.username.text.toString()
        data["password"] = binding.password.text.toString()
        data["source"] = "app"

        if (genderUserType.equals("faculty")) {
            apiManager!!.facultyLogin(data).enqueue(object : Callback<ResponseBody>     {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        if (jsonobject.getString("errorCode").equals("1"))
                            showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                        else {
                            showOtpDialog(this@LoginActivity,4){
                                if(it.equals("resend"))
                                    resendOtp(binding.username.text.toString())
                                else
                                    verifyOtp(it,binding.username.text.toString(),jsonobject,"faculty")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE"+t.printStackTrace())
                    binding.progressBar.visibility = View.GONE
                    showSnackBar("Network Issue! Please try again", binding.rlMain)

                }

            })
        }
        else if (genderUserType.equals("student")) {
            apiManager!!.studentLogin(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        if (jsonobject.getString("errorCode").equals("1"))
                            showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                        else {
                            /*showOtpDialog(this@LoginActivity,4){
                                if(it.equals("resend"))
                                    resendOtp(binding.username.text.toString())
                                else
                                    verifyOtp(it,binding.username.text.toString(),jsonobject,"student")
                            }*/
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"

                            Preferences.instance!!.userType =
                                "student"
                            Preferences.instance!!.email =
                                binding.username.text.toString()
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")
                            Preferences.instance!!.userName =
                                jsonobject.getJSONObject("responseObject").getString("name")

                            Preferences.instance!!.collegeRollNo =
                                jsonobject.getJSONObject("responseObject").getString("enrollmentNo")

                            Preferences.instance!!.savePreferences(this@LoginActivity)
                            showToast(jsonobject.getString("errorMessage"))
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    binding.progressBar.visibility = View.GONE
                    showSnackBar("Network Issue! Please try again", binding.rlMain)

                }

            })

        }


    }

    private fun resendOtp(email: String){
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["email"] = email
        data["userType"] = "Faculty"
        apiManager!!.resendLoginOtp(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE"+t.printStackTrace())
                    binding.progressBar.visibility = View.GONE
                    showSnackBar("Network Issue! Please try again", binding.rlMain)

                }

            })
    }

    private fun verifyOtp(otp: String,email:String,jsonobject: JSONObject,type:String) {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["email"] = email
        data["otp"] = otp
        data["userType"] = "Faculty"
        apiManager!!.verifyLoginOtp(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobjectOtp = JSONObject(stringResponse)
                    if (jsonobjectOtp.getString("errorCode").equals("1"))
                        showSnackBar(jsonobjectOtp.getString("errorMessage"), binding.rlMain)
                    else {
                        if(type.equals("faculty")){
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"
                            Preferences.instance!!.userName =
                                jsonobject.getJSONObject("responseObject").getString("name")
                            Preferences.instance!!.email =
                                jsonobject.getJSONObject("responseObject").getString("email")

                            Preferences.instance!!.gender =
                                jsonobject.getJSONObject("responseObject").getString("department")

                            Preferences.instance!!.userType =
                                "faculty"
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")
                            Preferences.instance!!.savePreferences(this@LoginActivity)
                            startActivity(Intent(this@LoginActivity, FacultyHomeActivity::class.java))
                            finish()
                        }
                        else{
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"

                            Preferences.instance!!.userType =
                                "student"
                            Preferences.instance!!.email =
                                binding.username.text.toString()
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")
                            Preferences.instance!!.userName =
                                jsonobject.getJSONObject("responseObject").getString("name")

                            Preferences.instance!!.collegeRollNo =
                                jsonobject.getJSONObject("responseObject").getString("enrollmentNo")

                            Preferences.instance!!.savePreferences(this@LoginActivity)
                            showToast(jsonobject.getString("errorMessage"))
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DATA", "FAILURE"+t.printStackTrace())
                binding.progressBar.visibility = View.GONE
                showSnackBar("Network Issue! Please try again", binding.rlMain)

            }

        })
    }

    fun showOtpDialog(
        context: Context,
        digits: Int = 4,
        onOtpEntered: (otp: String) -> Unit
    ) {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_otp, null)

        val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)
        //if (!phoneHint.isNullOrEmpty()) tvSubtitle.text = phoneHint

        val editTexts = listOf<EditText>(
            view.findViewById(R.id.etOtp1),
            view.findViewById(R.id.etOtp2),
            view.findViewById(R.id.etOtp3),
            view.findViewById(R.id.etOtp4)
        ).take(digits)

        editTexts.forEachIndexed { index, editText ->

            // Move to next when 1 digit entered
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < editTexts.size - 1) {
                        editTexts[index + 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // Backspace → go to previous field if empty
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() && index > 0) {
                        editTexts[index - 1].requestFocus()
                        editTexts[index - 1].setSelection(editTexts[index - 1].text.length)
                    }
                }
                false
            }
        }
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnVerify = view.findViewById<Button>(R.id.btnVerify)

        btnCancel.setOnClickListener {
            onOtpEntered("resend")
        }
        btnVerify.setOnClickListener {
            val otp = editTexts.joinToString("") { it.text.toString().trim() }
            if (otp.length == digits) {
                dialog.dismiss()
                onOtpEntered(otp)
            } else {
                editTexts.firstOrNull { it.text.isNullOrEmpty() }?.requestFocus()
            }
        }

        dialog.setContentView(view)
        dialog.show()

        // show keyboard for first field
        editTexts.firstOrNull()?.postDelayed({
            editTexts.firstOrNull()?.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editTexts.first(), InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    override fun onDestroy() {
        Log.e("DESTRO","DESTRO")
        super.onDestroy()
    }

}

