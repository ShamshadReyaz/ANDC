package com.mobiquel.dyalsinghapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobiquel.dyalsinghapp.R
import com.mobiquel.dyalsinghapp.databinding.ActivityHomeBinding
import com.mobiquel.dyalsinghapp.data.ApiManager
import com.mobiquel.dyalsinghapp.pojo.DostToenModel
import com.mobiquel.dyalsinghapp.utils.*
import com.mobiquel.dyalsinghapp.view.adapter.AssignmentsMessagesListAdapter
import com.mobiquel.dyalsinghapp.view.fragment.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var apiManager: ApiManager
    private var dostTokenResponse: String? = null
    private var notificationId = ""
    var context: Context? = null
    private lateinit var binding: ActivityHomeBinding
    private val fragmentNoticeFragment = NoticeFragment()
    private val fragmentAttendanceStudentFragment = StudentAttendanceFragment()
    private val fragmentStudentHomeFragment = StudentHomeFragment()
    private val fragmentProfileFragment = StudentProfileFragment()
    private val maintenanceFragment = MaintenanceFragment()
    private val wifiFragment = WifiFragment()
    private val webViewFragment = WebViewragment()

    private val fragmentSupportManager = supportFragmentManager
    var active: Fragment = fragmentNoticeFragment
    var editStatus = 0
    var hashMap: HashMap<String, Int>? = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set status bar color to match background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.parseColor("#F6DBDC")
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Handle window insets to prevent status bar overlap
        val originalPadding = resources.getDimensionPixelSize(R.dimen.d_10dp)
        ViewCompat.setOnApplyWindowInsetsListener(binding.topLayout2) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Add status bar height to top padding, keep other padding as original
            view.setPadding(
                originalPadding,
                systemBars.top + originalPadding,
                originalPadding,
                originalPadding
            )
            insets
        }
        
        // Request insets to be applied after view is laid out
        binding.root.post {
            ViewCompat.requestApplyInsets(binding.root)
        }

        hashMap = null


        Log.e("ON CREATE", "HOME")

        context = this@HomeActivity
        binding.version.text = "Version: " + getAppVersion()
        getNotificationId()
        getYourDostToken()


        if(intent?.extras?.getString("TYPE").equals("NOTIFICATION")){
            redirectToFragment("notice")
        }
        else{
            fragmentSupportManager.beginTransaction().apply {
                add(R.id.frameLayout, fragmentStudentHomeFragment, "0")
                commit()
            }

        }



        binding.edit.setOnClickListener {
            if (editStatus == 0) {
                fragmentProfileFragment.openEditMode()
                binding.edit.setImageResource(R.drawable.ic_done)
                editStatus = 1
            } else {
                binding.edit.setImageResource(R.drawable.ic_edit)
                fragmentProfileFragment.updateProfileCheck()
                editStatus = 0
            }

        }
        binding.counsellingPage.setOnClickListener {
            redirectToWeb2("https://yourdost.com/login/sso?token=" + dostTokenResponse)

        }
        binding.logout.setOnClickListener {


        }

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
                        if(!jsonobject.getString("responseObject").equals(getAppVersion())){
                            showAppUpdateDialog()
                        }
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
    fun showAppUpdateDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("App update available. Please update your app.")
        builder.setPositiveButton("Proceed") { dialogInterface, which ->
            dialogInterface.cancel()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.PLAY_STORE_URL))
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialogInterface, which ->
            dialogInterface.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
    fun getMessagesForStudentId() {
        Preferences.instance!!.loadPreferences(context!!)
        val data: MutableMap<String, String> = HashMap()
        data["studentId"] = Preferences.instance!!.userId!!
        binding.progressBar.visibility = View.VISIBLE
        apiManager!!.getMessagesForStudentId(data).enqueue(object : Callback<ResponseBody> {
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
                        if (jsonobject.getJSONArray("responseObject").length() > 0)
                            showAssignmentsMessagesDialog(jsonobject.getJSONArray("responseObject"))
                        else
                            showSnackBar("No Assignments/Messages found", binding.rlMain)
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

    fun getStudentAcademicDetailsByRollNo() {
        Preferences.instance!!.loadPreferences(context!!)
        val data: MutableMap<String, String> = HashMap()
        data["rollNo"] = Preferences.instance!!.collegeRollNo!!
        binding.progressBar.visibility = View.VISIBLE
        apiManager!!.getStudentAcademicDetailsByRollNo(data)
            .enqueue(object : Callback<ResponseBody> {
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
                            showAcademicDetailsDialog(jsonobject.getJSONObject("responseObject"))
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
        val f0: StudentHomeFragment? =
            supportFragmentManager.findFragmentByTag("0") as StudentHomeFragment?
        if (f0 != null && !f0.isVisible) {

            fragmentSupportManager.beginTransaction().apply {
                replace(R.id.frameLayout, fragmentStudentHomeFragment, "0")
                    .addToBackStack("0")
                commit()
            }
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
            "logout" -> {
                logout()
                binding.edit.visibility = View.GONE
            }

            "profile"-> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentProfileFragment, "2")
                        .addToBackStack("2")
                    commit()
                }
             //   binding.edit.visibility = View.VISIBLE
            }
            "maintenance" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, maintenanceFragment, "3")
                        .addToBackStack("3")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "wifi"-> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, wifiFragment, "4")
                        .addToBackStack("4")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "attendance" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentAttendanceStudentFragment, "5")
                        .addToBackStack("5")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "academic" -> {
                getStudentAcademicDetailsByRollNo()
            }
            "assignments" -> {
                getMessagesForStudentId()
            }
            "iarecord" -> {
                fragmentSupportManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fragmentAttendanceStudentFragment, "5")
                        .addToBackStack("5")
                    commit()
                }
                binding.edit.visibility = View.GONE
            }
            "" -> {
                Toast.makeText(this@HomeActivity, "Coming Soon!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun showAcademicDetailsDialog(jsonObject: JSONObject) {
        try {
            val dialogView: View = View.inflate(this, R.layout.dialog_show_academic_details, null)
            val dialog = BottomSheetDialog(this)

            var examRollNo =
                dialogView.findViewById<TextInputEditText>(R.id.examRollNo)
            var enrollmentNo =
                dialogView.findViewById<TextInputEditText>(R.id.enrollmentNo)
            var collegeRollNo =
                dialogView.findViewById<TextInputEditText>(R.id.collegeRollNo)
            var sectionName =
                dialogView.findViewById<TextInputEditText>(R.id.sectionName)
            var tutorialName =
                dialogView.findViewById<TextInputEditText>(R.id.tutorialName)
            examRollNo.setText(jsonObject.getString("examRollNo"))
            enrollmentNo.setText(jsonObject.getString("enrollmentNo"))
            collegeRollNo.setText(jsonObject.getString("collegeRollNo"))
            sectionName.setText(jsonObject.getString("section"))
            tutorialName.setText(jsonObject.getString("tutorial"))


            dialog!!.setContentView(dialogView)
            dialog!!.setCancelable(true)
            dialog!!.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun showAssignmentsMessagesDialog(jsonArray: JSONArray) {
        try {
            val dialogView: View =
                View.inflate(this, R.layout.dialog_show_assignments_messages, null)
            val dialog = BottomSheetDialog(this)

            var listView =
                dialogView.findViewById<RecyclerView>(R.id.listview)
            val mAdapter=AssignmentsMessagesListAdapter(context!!,jsonArray)
            listView.adapter=mAdapter
            listView.layoutManager=LinearLayoutManager(context!!)

            dialog!!.setContentView(dialogView)
            dialog!!.setCancelable(true)
            dialog!!.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    fun logout(){
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