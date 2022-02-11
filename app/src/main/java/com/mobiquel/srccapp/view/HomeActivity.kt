package com.mobiquel.srccapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.getAppVersion
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import com.mobiquel.srccapp.view.fragment.NoticeFragment
import com.mobiquel.srccapp.view.fragment.ProfileFragment
import com.mobiquel.srccapp.view.viewmodel.APIViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import srccapp.R
import srccapp.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private var notificationId = ""
    var context: Context? = null
    private lateinit var binding: ActivityHomeBinding
    private lateinit var apiViewModel: APIViewModel
    val fragmentNoticeFragment=NoticeFragment()
    val fragmentProfileFragment=ProfileFragment()
    val fragmentSupportManager= supportFragmentManager
    var active:Fragment=fragmentNoticeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("ON CREATE", "HOME")

        context = this@HomeActivity
        apiViewModel = APIViewModel()
        version.text = "Version: " + getAppVersion()
        getNotificationId()


        fragmentSupportManager.beginTransaction().apply {
            add(R.id.frameLayout, fragmentProfileFragment, "2")
            hide(fragmentProfileFragment)
            commit()
        }

        fragmentSupportManager.beginTransaction().apply {
            add(R.id.frameLayout, fragmentNoticeFragment, "1")
            commit()
        }




        binding.navView.setOnNavigationItemSelectedListener { it ->

            when (it.itemId) {
                R.id.navigation_notice -> {
                    fragmentSupportManager.beginTransaction().apply {
                        hide(active)
                        show(fragmentNoticeFragment)
                        commit()
                    }

                    active=fragmentNoticeFragment


                    true
                }

                R.id.navigation_profile -> {
                    fragmentSupportManager.beginTransaction().apply {
                        hide(active)
                        show(fragmentProfileFragment)
                        commit()
                    }
                    active=fragmentProfileFragment

                    true
                }
            }
            true
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
            val token = task.result
            if (token != null) {
                notificationId = token
                var model = CheckVersionModel()
                model.pushNotificationId = notificationId
                model.userId = Preferences!!.instance!!.userId
                model.userType = Preferences!!.instance!!.userType
                apiViewModel!!.checkSmartProfVersion(model!!)?.observe(this, Observer {
                    try {
                        val stringResponse = it.data!!.string()
                        val jsonobject = JSONObject(stringResponse)
                        if (jsonobject.getString("errorCode").equals("1"))
                            showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                        else {

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                })
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


}