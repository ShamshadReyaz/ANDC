package com.mobiquel.srccapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.ActivityLoginBinding
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.Security
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import com.mobiquel.srccapp.view.viewmodel.LoginAPIViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var context: Context? = null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiViewModel: LoginAPIViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this@LoginActivity
        binding.img.bringToFront()

        apiViewModel = LoginAPIViewModel()

        binding.btnSignIn.setOnClickListener {
            if (binding.userTypeGrp.checkedRadioButtonId == -1)
                showSnackBar("Please select user type!", binding.rlMain)
            else if (binding.username.text.toString().equals(""))
                showSnackBar("Please enter emailId!", binding.rlMain)
            else if (binding.password.text.toString().equals(""))
                showSnackBar("Please enter Password!", binding.rlMain)
            else {
                login()
/*
                    binding.progressBar.visibility=View.VISIBLE
                var genderUserType =
                    resources.getResourceEntryName(binding.userTypeGrp.checkedRadioButtonId) // "male"
                if (genderUserType.equals("nonTeaching"))
                    genderUserType = "non-teaching"

                apiViewModel.login(
                    genderUserType,
                    binding.username.text.toString(),
                    Security.encrypt(binding.password.text.toString())
                )?.observe(this,
                    androidx.lifecycle.Observer {
                        binding.progressBar.visibility=View.GONE
                        try {
                            val stringResponse = it.data?.string()
                            val jsonobject = JSONObject(stringResponse)
                            if (jsonobject.getString("errorCode").equals("1"))
                                showSnackBar(
                                    "Invalid Credentials! Please try again",
                                    binding.rlMain
                                )
                            else {
                                Preferences.instance!!.loadPreferences(this@LoginActivity)
                                Preferences.instance!!.isLoginDone = "1"

                                Preferences.instance!!.userType =
                                    genderUserType
                                Preferences.instance!!.email =
                                    binding.username.text.toString()
                                Preferences.instance!!.userId =
                                    jsonobject.getJSONObject("responseObject").getString("id")
                                if (genderUserType.equals("student")) {
                                    Preferences.instance!!.collegeRollNo =
                                        jsonobject.getJSONObject("responseObject")
                                            .getString("enrollmentNo")
                                }

                                Preferences.instance!!.savePreferences(this@LoginActivity)
                                showToast(jsonobject.getString("errorMessage"))
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                finish()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
*/

            }

        }


/*
        binding.userTypeGrp.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                val userType =
                    resources.getResourceEntryName(p1) // "male"
                if (userType.equals("faculty")) {
                    binding.username.setText("aasheerwad.dwivedi@srcc.edu")
                    binding.password.setText("gojJ379")
                }
                if (userType.equals("student")) {
                    binding.username.setText("20ba003@srcc.edu")
                    binding.password.setText("KDAS931")
                }
                if (userType.equals("nonTeaching")) {
                    binding.username.setText("neha.sharma@srcc.du.ac.in")
                    binding.password.setText("srcc@123")
                }

            }

        })

        binding.faculty.isChecked = true
*/


    }

    fun login() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["email"] = binding.username.text.toString()
        data["password"] = Security.encrypt(binding.password.text.toString())
        val apiManager: ApiManager? = ApiManager.init()

        val genderUserType =
            resources.getResourceEntryName(binding.userTypeGrp.checkedRadioButtonId) // "male"
        if (genderUserType.equals("faculty")) {
            apiManager!!.facultyLogin(data).enqueue(object : Callback<ResponseBody> {
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
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"

                            Preferences.instance!!.userType =
                                "faculty"
                            Preferences.instance!!.email =
                                binding.username.text.toString()
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")
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
                }

            })

        } else if (genderUserType.equals("student")) {
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
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"

                            Preferences.instance!!.userType =
                                "student"
                            Preferences.instance!!.email =
                                binding.username.text.toString()
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")
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
                }

            })

        } else {
            apiManager!!.nonTeachingLogin(data).enqueue(object : Callback<ResponseBody> {
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
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                            Preferences.instance!!.isLoginDone = "1"

                            Preferences.instance!!.userType =
                                "non-teaching"
                            Preferences.instance!!.email =
                                binding.username.text.toString()
                            Preferences.instance!!.userId =
                                jsonobject.getJSONObject("responseObject").getString("id")

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
                }

            })

        }

    }


}

