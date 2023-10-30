package com.mobiquel.srccapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.data.NameIdPojo
import com.mobiquel.srccapp.databinding.ActivityHomeFacultyBinding
import com.mobiquel.srccapp.pojo.DostToenModel
import com.mobiquel.srccapp.room.viewmodel.AttendanceViewModel
import com.mobiquel.srccapp.utils.*
import com.mobiquel.srccapp.view.fragment.*
import com.mobiquel.srccapp.view.viewmodel.HomeAPIViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class FacultyHomeActivity : AppCompatActivity() {
    private var dostTokenResponse: String? = null
    private var notificationId = ""
    var context: Context? = null
    private lateinit var binding: ActivityHomeFacultyBinding
    private lateinit var apiViewModel: HomeAPIViewModel
    private val fragmentNoticeFragment = NoticeFragment()
  //  private val fragmentAttendanceFragment = AttendanceFragment()
    private val fragmentProfileFragment = ProfileFragment()
    private val maintenanceFragment = MaintenanceFragment()
    private val wifiFragment = WifiFragment()
    private val webViewFragment = WebViewragment()
    private val facultyHomeFragment = FacultyHomeFragment()
    private val offlineFragment = OfflineFragment()

    private val fragmentSupportManager = supportFragmentManager
    var active: Fragment = fragmentNoticeFragment
    var editStatus = 0
    var hashMap: HashMap<String, Int>? = HashMap()

    private var listOfGroupName = ArrayList<String>()
    private var listOfGroupId = ArrayList<NameIdPojo>()

    private var listOfPaperName = ArrayList<String>()
    private var listOfPaperId = ArrayList<NameIdPojo>()
    private var paperAdapter: ArrayAdapter<String>? = null

    var datePickerDialog: DatePickerDialog? = null
    var cal = Calendar.getInstance()

    var attendancePreviousFragmentType = "online"

    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var internetReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attendanceViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)

        hashMap = null


        Log.e("ON CREATE", "HOME")

        context = this@FacultyHomeActivity
        apiViewModel = HomeAPIViewModel()
        version.text = "Version: " + getAppVersion()
        getNotificationId()
        getGroup("")
        getYourDostToken()

        if(intent?.extras?.getString("TYPE").equals("NOTIFICATION")){
            redirectToFragment("notice")
        }
        else{
            fragmentSupportManager.beginTransaction().apply {
                add(R.id.frameLayout, facultyHomeFragment, "0")
                commit()
            }

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

            redirectToWeb2("https://yourdost.com/login/sso?token=" + dostTokenResponse)

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
                Preferences.instance!!.savePreferences(this@FacultyHomeActivity)
                showToast("Logout Successful!")
                startActivity(Intent(this@FacultyHomeActivity, LoginActivity::class.java))
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


        /*  receiver = InternetConnectivityReceiver()
          val filter = IntentFilter()
          filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
          registerReceiver(receiver, filter);*/

        /*internetReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                *//*val sms = arg1.extras!!.getString("m")
                intext.setText(sms)*//*
                if (isNetworkAvailable()) {
                    Log.e("INTENET PRESENT", "TRUE")
                    syncData()
                } else
                    Log.e("INTENET PRESENT", "FALSE")


            }
        }
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(internetReceiver, filter);
*/
        if (Build.VERSION.SDK_INT >= 31) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    1
                )
            }
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
                Log.e("TOKEN",token)
                checkSmartProfVersion()

            }

        })

    }

    override fun onStart() {
        super.onStart()
        Log.e("START", "HOME")
    }

    override fun onResume() {
        //syncData()
        super.onResume()
        Log.e("RESUME", "HOME")
    }

    override fun onPause() {
       // unregisterReceiver(internetReceiver);
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
        val dostTokenModel = DostToenModel()
        dostTokenModel.email = Preferences.instance!!.email!!
        dostTokenModel.organizationID = 56

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getYourDostToken(dostTokenModel).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    dostTokenResponse = response.body()?.string()
                    Log.e("RESPO_DOST", dostTokenResponse!!)
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

    override fun onBackPressed() {
        //super.onBackPressed()
        goToHomePage()

    }


    fun goToHomePage() {
        val f0: FacultyHomeFragment? =
            supportFragmentManager.findFragmentByTag("0") as FacultyHomeFragment?
        val f1: AttendanceFragment? =
            supportFragmentManager.findFragmentByTag("4") as AttendanceFragment?
        if (f1 != null && f1.isVisible) {

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            //set message for alert dialog
            builder.setMessage("Are you sure you want to exit attendance? Unsaved data will be lost.")
            // Create the AlertDialog
            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                dialogInterface.cancel()
                /*fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, offlineFragment, "5")
                        .addToBackStack("5")
                    commit()
                }*/
                if(attendancePreviousFragmentType.equals("online"))
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, facultyHomeFragment, "0")
                            .addToBackStack("0")
                        commit()
                    }
                else
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, offlineFragment, "5")
                            .addToBackStack("5")
                        commit()
                    }
                binding.edit.visibility = View.GONE
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.cancel()

            }

            val alertDialog = builder.create()


            // Set other dialog properties
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
        else if (f0 != null && !f0.isVisible) {

            fragmentSupportManager.beginTransaction().apply {
                replace(R.id.frameLayout, facultyHomeFragment, "0")
                    .addToBackStack("0")
                commit()
            }
            attendancePreviousFragmentType="online"
            binding.edit.visibility = View.GONE
        } else
            finish()


    }

    fun redirectToFragment(type: String) {
        when (type) {
            "notice" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentNoticeFragment, "1")
                        .addToBackStack("1")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "profile" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentProfileFragment, "2")
                        .addToBackStack("2")
                    commit()
                }
                binding.edit.visibility = View.VISIBLE
            }
            "maintenance" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, maintenanceFragment, "3")
                        .addToBackStack("3")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "home" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, facultyHomeFragment, "0")
                        .addToBackStack("0")
                    commit()
                }
                attendancePreviousFragmentType="online"
                binding.edit.visibility = View.GONE
            }
            "attendance" -> {
                if (listOfGroupName.size > 0)
                    showPaperGroupDialog()
                else if (isNetworkAvailable())
                    getGroup("SHOW")
                else
                    showSnackBar("Internet not available.", binding.rlMain)
            }
            "offline" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, offlineFragment, "5")
                        .addToBackStack("5")
                    commit()
                }
                attendancePreviousFragmentType="offline"
                binding.edit.visibility = View.GONE
            }
            "onoff" -> {
                if(attendancePreviousFragmentType.equals("online"))
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, facultyHomeFragment, "0")
                        .addToBackStack("0")
                    commit()
                }
                else
                    fragmentSupportManager.beginTransaction().apply {
                        replace(R.id.frameLayout, offlineFragment, "5")
                            .addToBackStack("5")
                        commit()
                }
                /*attendancePreviousFragmentType="offline"
                binding.edit.visibility = View.GONE*/
            }
            "" -> {
                Toast.makeText(this@FacultyHomeActivity, "Coming Soon!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun showPaperGroupDialog() {
        try {
            val dialogView: View = View.inflate(this, R.layout.dialog_paper_group_date, null)
            val dialog = BottomSheetDialog(this)

            var group =
                dialogView.findViewById<AutoCompleteTextView>(R.id.group)
            var paper =
                dialogView.findViewById<AutoCompleteTextView>(R.id.paper)
            var dateOfAttendance =
                dialogView.findViewById<EditText>(R.id.dateOfAttendance)
            var dateOfAttendancelay =
                dialogView.findViewById<TextInputLayout>(R.id.dateOfAttendanceLayout)
            var proceed =
                dialogView.findViewById<TextView>(R.id.proceed)
            var groupAdapter =
                ArrayAdapter(
                    this,
                    R.layout.list_item_dropdown,
                    listOfGroupName!!
                )
            paperAdapter =
                ArrayAdapter(
                    this,
                    R.layout.list_item_dropdown,
                    listOfPaperName!!
                )
            group.setAdapter(groupAdapter)
            paper.setAdapter(paperAdapter)

            group.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isNetworkAvailable()) {
                        val modelClass =
                            listOfGroupId!!.find { it.name.equals(group.text.toString()) }
                        getPaper(modelClass!!.id)
                    } else
                        showToast("Internet not available.")

                }

            })
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd MMM, yyyy" +
                            "" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    dateOfAttendance.setText(sdf.format(cal.time))
                }
            dateOfAttendance.setOnClickListener {
                cal = Calendar.getInstance()
                cal.timeZone= TimeZone.getTimeZone("Asia/Calcutta")
                datePickerDialog = DatePickerDialog(
                    this,
                    R.style.MyDatePickerStyle,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog!!.getDatePicker().maxDate=cal.timeInMillis;
                datePickerDialog!!.show();
            }
            proceed.setOnClickListener {
                val fragmentAttendanceFragment = AttendanceFragment()
                val grpId = listOfGroupId!!.find { it.name.equals(group.text.toString()) }!!.id
                val paperId = listOfPaperId!!.find { it.name.equals(paper.text.toString()) }!!.id
                val bundle=Bundle()
                bundle.putString("GRPID",grpId)
                bundle.putString("PAPRID",paperId)
                bundle.putString("GRPNAME",group.text.toString())
                bundle.putString("PAPRNAME",paper.text.toString())
                bundle.putString("DATE",dateOfAttendance.text.toString())
                fragmentAttendanceFragment.arguments=bundle
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentAttendanceFragment, "4")
                        .addToBackStack("4")
                    commit()
                }
                attendancePreviousFragmentType="online"
                dialog!!.cancel()
            }

            dialog!!.setContentView(dialogView)
            dialog!!.setCancelable(true)
            dialog!!.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun redirectToAttendacePage(bundle: Bundle) {
        val fragmentAttendanceFragment = AttendanceFragment()
        fragmentAttendanceFragment.arguments = bundle
        fragmentSupportManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragmentAttendanceFragment, "4")
                .addToBackStack("4")
            commit()
        }
    }

    fun getGroup(type: String) {
        if (type.equals("SHOW"))
            binding.progressBar.visibility = View.VISIBLE
        Preferences.instance!!.loadPreferences(context!!)
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getVirtualClassForFaculty(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("0")) {
                        listOfGroupId.clear()
                        listOfGroupName.clear()
                        val grpJsonArray = jsonobject.getJSONArray("responseObject")
                        for (i in 0 until grpJsonArray.length()) {
                            listOfGroupId.add(
                                NameIdPojo(
                                    grpJsonArray.getJSONObject(i).getString("virtualGroupName"),
                                    grpJsonArray.getJSONObject(i).getString("virtualGroupId")
                                )
                            )
                            listOfGroupName.add(
                                grpJsonArray.getJSONObject(i).getString("virtualGroupName")
                            )
                        }
                        if (type.equals("SHOW"))
                            showPaperGroupDialog()

                    } else if (jsonobject.getString("errorCode").equals("1"))
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

    fun getPaper(virtualGroupId: String) {
        Preferences.instance!!.loadPreferences(context!!)
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        data["virtualGroupId"] = virtualGroupId

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getPapersForFacultyByVirtualGroup(data)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)
                        listOfPaperId.clear()
                        listOfPaperName.clear()
                        if (jsonobject.getString("errorCode").equals("0")) {

                            val grpJsonArray = jsonobject.getJSONArray("responseObject")
                            for (i in 0 until grpJsonArray.length()) {
                                listOfPaperId.add(
                                    NameIdPojo(
                                        grpJsonArray.getJSONObject(i).getString("subject"),
                                        grpJsonArray.getJSONObject(i).getString("paperId")
                                    )
                                )
                                listOfPaperName.add(
                                    grpJsonArray.getJSONObject(i).getString("subject")
                                )

                            }
                            paperAdapter!!.notifyDataSetChanged()

                        } else if (jsonobject.getString("errorCode").equals("1"))
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

    fun syncData() {
        if (isNetworkAvailable()) {
            runBlocking {
                attendanceViewModel.syncData(this@FacultyHomeActivity)
                /*val f1: OfflineFragment? =
                    supportFragmentManager.findFragmentByTag("5") as OfflineFragment?
                if (f1!=null && f1!!.isVisible)
                    f1!!.getOfflineData()*/
            }
            /*lifecycleScope.launch {

            }*/

        }
    }


}