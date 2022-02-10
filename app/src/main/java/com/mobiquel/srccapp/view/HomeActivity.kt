package com.mobiquel.srccapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.getAppVersion
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import srccapp.R
import srccapp.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private var notificationId = ""
    var context: Context? = null
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this@HomeActivity

        GlobalScope.launch {
            version.text = "Version: " + getAppVersion()
            getNotificationId()


            val navView: BottomNavigationView = binding.navView

            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)

        }


        logout.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("LOG OUT")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to logout?")
            // Create the AlertDialog
            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                dialogInterface.cancel()
                Preferences.instance!!.isLoginDone = "0"
                Preferences.instance!!.savePreferences(this@HomeActivity)
                showToast("Logout Successful!")
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.cancel()
            }

            val alertDialog = builder.create()


            // Set other dialog properties
            alertDialog.setCancelable(true)
            alertDialog.show()


        }

    }


    fun getNotificationId() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("NOTIFICATION_ID", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            if (token != null) {
                notificationId = token
                checkSmartProfVersion()
            }

        })
    }


    fun checkSmartProfVersion() {
        Preferences.instance!!.loadPreferences(context!!)
        val data: MutableMap<String, String> = HashMap()
        data["userId"] = Preferences.instance!!.userId!!
        data["userType"] = Preferences.instance!!.userType!!
        data["os"] = "Android"
        data["pushNotificationId"] = notificationId

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.checkSmartProfVersion(data).enqueue(object : Callback<ResponseBody> {
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
                        /*   var dataList = ArrayList<String>()
                           for (i in 0 until jsonobject.getJSONArray("responseObject").length()) {
                               dataList.add(
                                   jsonobject.getJSONArray("responseObject").getJSONObject(i)
                                       .toString()
                               )
                           }
                           val mAdapter = NoticeListAdapter(this@HomeActivity, dataList)
                           binding.listView.layoutManager = LinearLayoutManager(
                               this@HomeActivity,
                               LinearLayoutManager.VERTICAL,
                               false
                           )
                           binding.listView.adapter = mAdapter

                           if (dataList.size == 0)
                               binding.noResult.visibility = View.VISIBLE
   */
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