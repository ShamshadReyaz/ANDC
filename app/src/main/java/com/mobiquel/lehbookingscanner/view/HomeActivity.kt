package com.mobiquel.lehbookingscanner.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.zxing.integration.android.IntentIntegrator
import com.mobiquel.lehbookingscanner.R
import com.mobiquel.lehbookingscanner.data.ApiManager
import com.mobiquel.lehbookingscanner.data.DataStoreManager
import com.mobiquel.lehbookingscanner.databinding.ActivityHomeBinding
import com.mobiquel.lehbookingscanner.service.Supplier
import com.mobiquel.lehbookingscanner.utils.Security
import com.mobiquel.lehbookingscanner.utils.getAppVersion
import com.mobiquel.lehbookingscanner.utils.showSnackBar
import com.mobiquel.lehbookingscanner.view.adapter.CircuitListAdapter
import com.mobiquel.lehbookingscanner.view.adapter.PassengerListAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class HomeActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST = 0
    private var notificationId = ""
    private var deviceId = ""
    private var deviceModel = ""
    private var deviceMake = ""
    private var vehicleType = ""
    private lateinit var passengerListAdapter:PassengerListAdapter
    var context: Context? = null
    lateinit var dataStoreManager: DataStoreManager
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this@HomeActivity
        dataStoreManager = DataStoreManager(this@HomeActivity)


        GlobalScope.launch {
            binding.scan.setOnClickListener {
                if(scan.text.toString().equals("SCAN NEW")){
                    topLayout.visibility=View.VISIBLE
                    scan.setText("SCAN PERMIT")
                    bookinId.setText("")
                    correctLayout.visibility=View.GONE
                    checkinPassengers.visibility = View.GONE
                }
                else{
                    vehicleType=vehicleDropdown.selectedItem.toString()
                    IntentIntegrator(this@HomeActivity).initiateScan()
                    lot1.visibility = View.VISIBLE
                    correctLayout.visibility = View.GONE
                    wrongLayout.visibility = View.GONE
                }

            }
            // version.setText("Version: "+getVersion())
            getNotificationId()

        }

        GlobalScope.launch {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        this@HomeActivity,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@HomeActivity, arrayOf(Manifest.permission.READ_PHONE_STATE),
                        PERMISSION_REQUEST
                    )
                } else {
                    version.setText("App Ver. "+getAppVersion())
                }
            } else {
                version.setText("App Ver. "+getAppVersion())
            }

        }

        logout.setOnClickListener {
            GlobalScope.launch {
                var status = false;
                dataStoreManager.userLoginStats.asLiveData().observe(this@HomeActivity) {
                    status = it
                }
                Log.e("DATA====", "STORE===== " + status)
                dataStoreManager.storeUser(
                    "",
                    "",
                    "",
                    false
                )

            }
            showToast("Logout Successful!")
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }

        getBookinDetails.setOnClickListener {
            vehicleType=vehicleDropdown.selectedItem.toString()
            if (bookinId.text.toString().equals(""))
                showSnackBar("Please enter PNR", bookinId)
            else
                getBookingDetails(bookinId.text.toString())
        }

        checkinPassengers.setOnClickListener {
            checkInPassengers()
        }

        version.visibility=View.GONE

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {

                //showToast("Cancelled")
            } else {

                //showToast("Scanned")
                try {
                 //   var data = Security.decrypt(result.contents.replace(" ", "+"))
                    try {
                        Log.e("VALID DATA", "===== " + result.contents);
                        openPermitScanResult(result.contents)
                    } catch (e: JSONException) {
                        showToast("Invalid Permit!")
                    }
                } catch (e: java.lang.Exception) {
                    lot1.visibility = View.GONE
                    correctLayout.visibility = View.GONE
                    wrongLayout.visibility = View.VISIBLE
                    lot2.playAnimation()
                }


            }
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun openPermitScanResult(data: String) {
        lot1.visibility = View.GONE
        try {
            bookinId.setText(data)
            getBookingDetails(data)

        } catch (e: Exception) {
            e.printStackTrace()
            correctLayout.visibility = View.GONE
            wrongLayout.visibility = View.VISIBLE
            lot2.playAnimation()
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
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //getAppVersion()
                }
                return
            }

        }
    }

    fun getBookingDetails(bookingId: String) {
        progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["bookingCode"] = bookingId

        val apiManager: ApiManager? = ApiManager.init()
        if (vehicleType.equals("Helicopter")) {
            apiManager!!.getBookingByCodeHeli(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        if (jsonobject.getString("errorCode").equals("1")) {
                            showSnackBar("Invalid Booking ID!", binding.rlMain)
                            lot1.visibility = View.GONE
                            correctLayout.visibility = View.GONE
                            wrongLayout.visibility = View.VISIBLE
                            lot2.playAnimation()

                            Handler().postDelayed({
                                wrongLayout.visibility = View.GONE
                                lot1.visibility = View.VISIBLE

                            },3000)

                        } else {
                            setUpData(jsonobject.getJSONObject("responseObject"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    progressBar.visibility = View.GONE
                }

            })

        } else {
            apiManager!!.getBookingByCodeArmy(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        if (jsonobject.getString("errorCode").equals("1")) {
                            showSnackBar("Invalid Booking ID!", binding.rlMain)
                            lot1.visibility = View.GONE
                            correctLayout.visibility = View.GONE
                            wrongLayout.visibility = View.VISIBLE
                            lot2.playAnimation()

                            Handler().postDelayed({
                                wrongLayout.visibility = View.GONE
                                lot1.visibility = View.VISIBLE

                            },3000)

                        } else {
                            setUpData(jsonobject.getJSONObject("responseObject"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    progressBar.visibility = View.GONE
                }

            })
        }
    }

    fun checkInPassengers() {
        progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["passengerIds"] = passengerListAdapter.checkedPassenId
        data["bookingCode"] = bookinId.text.toString()

        val apiManager: ApiManager? = ApiManager.init()
        if (vehicleType.equals("Helicopter")) {
            apiManager!!.checkInHeli(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        //if (jsonobject.getString("errorCode").equals("1")) {
                            showSnackBar(jsonobject.getString("errorMessage"), binding.rlMain)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    progressBar.visibility = View.GONE
                }

            })

        } else {
            apiManager!!.checkInArmy(data).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                      //  if (jsonobject.getString("errorCode").equals("1")) {
                            showSnackBar(jsonobject.getString("errorMessage"), binding.rlMain)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    progressBar.visibility = View.GONE
                }

            })
        }
    }


    fun setUpData(jsonObject: JSONObject) {
        try {
            var listOfAgentString: MutableList<String>? = null
            listOfAgentString = ArrayList()


            for (i in 0..jsonObject.getJSONArray("passengerList").length().minus(1)) {

                listOfAgentString.add(
                    jsonObject.getJSONArray("passengerList").getJSONObject(i).toString()
                )
                if(jsonObject.getJSONArray("passengerList").getJSONObject(i).getString("isCheckedIn").equals("T"))
                    checkinPassengers.visibility=View.GONE
                else
                    checkinPassengers.visibility=View.VISIBLE

            }



            passengerListAdapter = listOfAgentString?.let { PassengerListAdapter(this,vehicleType, it) }
            passengerList?.layoutManager = LinearLayoutManager(this)
            passengerList?.adapter = passengerListAdapter
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val formatted = SimpleDateFormat("dd MMM, yyyy")
            try {
                bookingId.setText(bookinId.text.toString())
                primaryMobile.setText(jsonObject.getString("primaryMobile"))
                source.setText(jsonObject.getString("source"))
                destination.setText(jsonObject.getString("destination"))
                if(jsonObject.has("flightType"))
                 flightType.setText(jsonObject.getString("flightType"))
                else
                    flightType.setText("N/A")

                if(vehicleType.equals("Helicopter"))
                {
                    startDate.setText(
                        formatted.format(
                            sdf.parse(
                                jsonObject.getString("startDate")
                            )
                        )
                    )
                }
                else{
                    startDate.setText(
                        formatted.format(
                            sdf.parse(
                                jsonObject.getString("journeyDate")
                            )
                        )
                    )
                }

                endDate.setText(jsonObject.getString("primaryPassenger"))

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            if (listOfAgentString.size == 0) {
                lot1.visibility=View.GONE
                correctLayout.visibility = View.GONE
                wrongLayout.visibility = View.VISIBLE
                lot2.playAnimation()

                Handler().postDelayed({
                    wrongLayout.visibility = View.GONE
                    lot1.visibility = View.VISIBLE

                },3000)

            } else {


                wrongLayout.visibility = View.GONE
                topLayout.visibility = View.GONE
                correctLayout.visibility = View.VISIBLE
               // checkinPassengers.visibility = View.VISIBLE
                scan.setText("SCAN NEW")
                lot3.playAnimation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}