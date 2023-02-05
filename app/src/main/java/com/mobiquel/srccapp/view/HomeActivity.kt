package com.mobiquel.srccapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.ActivityHomeBinding
import com.mobiquel.srccapp.pojo.DostToenModel
import com.mobiquel.srccapp.utils.*
import com.mobiquel.srccapp.view.fragment.*
import com.mobiquel.srccapp.view.viewmodel.HomeAPIViewModel
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private var dostTokenResponse: String?=null
    private var notificationId = ""
    var context: Context? = null
    private lateinit var binding: ActivityHomeBinding
    private lateinit var apiViewModel: HomeAPIViewModel
    private val fragmentNoticeFragment = NoticeFragment()
    private val fragmentProfileFragment = ProfileFragment()
    private val maintenanceFragment = MaintenanceFragment()
    private val wifiFragment = WifiFragment()
    private val webViewFragment = WebViewragment()

    private val fragmentSupportManager = supportFragmentManager
    var active: Fragment = fragmentNoticeFragment
    var editStatus = 0
    var hashMap: HashMap<String, Int>? = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hashMap = null


        Log.e("ON CREATE", "HOME")

        context = this@HomeActivity
        apiViewModel = HomeAPIViewModel()
        version.text = "Version: " + getAppVersion()
        getNotificationId()
        getYourDostToken()
        if (Preferences!!.instance!!.userType.equals("student")) {
            binding.navView.menu.findItem(R.id.navigation_wifi_tab).isVisible = true
        }

        fragmentSupportManager.beginTransaction().apply {
            add(R.id.frameLayout, fragmentNoticeFragment, "1")
            commit()
        }

        binding.navView.setOnNavigationItemSelectedListener { it ->

            when (it.itemId) {
                R.id.navigation_notice -> {
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, fragmentNoticeFragment, "1")
                            .addToBackStack("1")
                        commit()
                    }
                    active = fragmentNoticeFragment
                    binding.edit.visibility = View.GONE
                    true
                }

                R.id.navigation_profile -> {
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, fragmentProfileFragment, "2")
                            .addToBackStack("2")
                        commit()
                    }
                    active = fragmentProfileFragment
                    binding.edit.visibility = View.VISIBLE
                    true
                }
                R.id.navigation_maintain -> {
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, maintenanceFragment, "3")
                            .addToBackStack("3")
                        commit()
                    }
                    active = maintenanceFragment
                    binding.edit.visibility = View.GONE
                    true
                }
                R.id.navigation_wifi_tab -> {
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, wifiFragment, "4")
                            .addToBackStack("4")
                        commit()
                    }
                    active = wifiFragment
                    binding.edit.visibility = View.GONE
                    true
                }
            }
            true
        }


        edit.setOnClickListener {
            if (editStatus == 0) {
                fragmentProfileFragment.openEditMode()
                edit.setImageResource(R.drawable.ic_done)
                editStatus = 1
            } else {
                edit.setImageResource(R.drawable.ic_edit)
                fragmentProfileFragment.updateProfileCheck()
                editStatus = 0
            }

        }
        counsellingPage.setOnClickListener {
           /* val bundle=Bundle()
            bundle.putString("TOKEN",dostTokenResponse)
            webViewFragment.arguments=bundle
            fragmentSupportManager.beginTransaction().apply {
                replace(R.id.frameLayout, webViewFragment, "5")
                    .addToBackStack("5")
                commit()
            }
            active = webViewFragment
            binding.edit.visibility = View.GONE
            binding.counsellingPage.visibility = View.GONE
            binding.navView.visibility = View.GONE
           */
        redirectToWeb2("https://yourdost.com/login/sso?token="+dostTokenResponse)

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
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("NOTIFICATION_ID", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            if (token != null) {
                notificationId = token
                checkSmartProfVersion()

            }

        })

    }

    override fun onStart() {
        super.onStart()
        Log.e("START", "HOME")
    }

    override fun onResume() {
        super.onResume()
        Log.e("RESUME", "HOME")
    }

    override fun onPause() {
        super.onPause()
        Log.e("PAUSE", "HOME")
    }

    override fun onStop() {
        super.onStop()
        Log.e("STOP", "HOME")
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


    fun getYourDostToken() {
        Preferences.instance!!.loadPreferences(context!!)
        val dostTokenModel=DostToenModel()
        dostTokenModel.email=Preferences.instance!!.email!!
        dostTokenModel.organizationID=56

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getYourDostToken(dostTokenModel).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        dostTokenResponse = response.body()?.string()
                        Log.e("RESPO_DOST",dostTokenResponse!!)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DATA", "FAILURE")
                binding.progressBar.visibility = View.GONE
            }

        })

    }
    override fun onBackPressed() {
        //super.onBackPressed()
        goToHomePage()

    }

    fun goToHomePage() {
        val f0: WebViewragment? =
            supportFragmentManager.findFragmentByTag("5") as WebViewragment?
        if (f0 != null && f0.isVisible) {
            if (fragmentSupportManager.backStackEntryCount > 0)
            {
                //getSupportFragmentManager().beginTransaction().remove(f0).commit();
                fragmentSupportManager.popBackStackImmediate("5",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            binding.edit.visibility = View.GONE
            binding.counsellingPage.visibility = View.VISIBLE
            binding.navView.visibility = View.VISIBLE
        } else
            finish()


    }

}