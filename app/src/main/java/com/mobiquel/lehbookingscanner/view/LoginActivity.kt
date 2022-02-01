package com.mobiquel.lehbookingscanner.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobiquel.lehbookingscanner.databinding.ActivityLoginBinding
import com.mobiquel.lehbookingscanner.data.ApiManager
import com.mobiquel.lehbookingscanner.data.DataStoreManager
import com.mobiquel.lehbookingscanner.utils.Preferences
import com.mobiquel.lehbookingscanner.utils.Security
import com.mobiquel.lehbookingscanner.utils.showSnackBar
import com.mobiquel.lehbookingscanner.utils.showToast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var context: Context? = null
    private lateinit var binding: ActivityLoginBinding
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this@LoginActivity
//http://139.59.81.101/cablecar/assets/front/images/loader.gif
        dataStoreManager = DataStoreManager(this@LoginActivity)

        binding.btnSignIn.setOnClickListener {

            if (binding.username.text.toString().length != 10)
                showSnackBar("Please enter correct mobile number!", binding.rlMain)
            if (binding.password.text.toString().equals(""))
                showSnackBar("Please enter Password!", binding.rlMain)
            else {
               /* startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
               */
             login()
            }

        }

    }

    fun login() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["mobile"] = binding.username.text.toString()
        data["password"] = Security.encrypt(binding.password.text.toString())

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.fieldStaffLogin(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("1"))
                        showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                    else {

                          /*  GlobalScope.launch {
                                dataStoreManager.storeUser(
                                        jsonobject.getJSONObject("responseObject").getString("name"),
                                        binding.username.text.toString(),
                                        jsonobject.getJSONObject("responseObject").getString("id"),
                                        true
                                    )

                            }
                        */
                            Preferences.instance!!.loadPreferences(this@LoginActivity)
                        Preferences.instance!!.isLoginDone="1"
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

