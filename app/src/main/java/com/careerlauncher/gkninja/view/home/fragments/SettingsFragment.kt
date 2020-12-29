package com.careerlauncher.gkninja.view.home.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseFragment
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.utils.AppUtils
import com.careerlauncher.gkninja.utils.CircleTransform
import com.careerlauncher.gkninja.utils.Preferences
import com.careerlauncher.gkninja.utils.getAppVersion
import com.careerlauncher.gkninja.utils.service.SoundService
import com.careerlauncher.gkninja.view.home.HomePresenter
import com.careerlauncher.gkninja.view.home.HomeView
import com.careerlauncher.gkninja.view.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject

/**
 * Created by Arman Reyaz on 12/19/2020.
 */
class SettingsFragment : BaseFragment(), HomeView {
    var profile: ImageView? = null
    var r1: RelativeLayout? = null
    var parentLayout: RelativeLayout? = null
    var myProfile: LinearLayout? = null
    var aboutApp: LinearLayout? = null
    var termsCondition: LinearLayout? = null
    var privacyPolicy: LinearLayout? = null
    var versionName: TextView? = null
    var logout: LinearLayout? = null
    var soundOnOff: SwitchCompat? = null
    var darkMode: SwitchCompat? = null
    var allowNotification: SwitchCompat? = null
    var layNotification: LinearLayout? = null
    var userName: TextView? = null
    var labelVal = ""
    var mHomePresenter: HomePresenter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_settings, container, false)

        if(isAdded){
            profile = view.findViewById(R.id.profile)
            r1 = view.findViewById(R.id.r1)
            parentLayout = view.findViewById(R.id.parentLayout)
            myProfile = view.findViewById(R.id.lay1)
            aboutApp = view.findViewById(R.id.lay2)
            termsCondition = view.findViewById(R.id.lay3)
            privacyPolicy = view.findViewById(R.id.lay4)
            versionName = view.findViewById(R.id.versionName)
            logout = view.findViewById(R.id.logout)
            soundOnOff = view.findViewById(R.id.soundOnOff)
            allowNotification = view.findViewById(R.id.allowNotification)
            layNotification = view.findViewById(R.id.lay_notification)
            userName = view.findViewById(R.id.name)
            darkMode = view.findViewById(R.id.darkMode)
            mHomePresenter = HomePresenter(this)

            Preferences.instance?.loadPreferences(activity as Context)
            val gender = Preferences.instance?.gender
            if (gender == "Male") Glide.with(activity)
                .load<Any>(R.drawable.male_img)
                .transform(CircleTransform(activity))
                .into(profile)
            else Glide.with(activity)
                .load<Any>(R.drawable.female_img)
                .transform(CircleTransform(activity))
                .into(profile)

            if (Preferences.instance?.gameSound.equals("1"))
                soundOnOff?.isChecked = true
            else
                soundOnOff?.isChecked = false

            soundOnOff?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (soundOnOff?.isPressed == true) {
                    if (isChecked == false) {
                        Preferences.instance?.gameSound = "0"
                        activity?.stopService(Intent(activity, SoundService::class.java))
                    } else {
                        Preferences.instance?.gameSound = "1"
                        activity?.startService(Intent(activity, SoundService::class.java))
                    }
                    Preferences.instance?.savePreferences(activity as Context)
                }
            })

            darkMode!!.setOnCheckedChangeListener { buttonView, isChecked ->
                if (darkMode!!.isPressed) {
                    if (isChecked == false) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) else AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                }
            }


            userName?.setText(DataManager(activity as Context).getStringFromPreference(PrefKeys.USER_NAME))
            mHomePresenter?.getTermsAndCondition()
            versionName?.text=activity?.getAppVersion()

            myProfile?.setOnClickListener {
                mHomePresenter?.getProfileData()
            }
            aboutApp?.setOnClickListener {
                showAboutAppBottomDialog()
            }
            termsCondition?.setOnClickListener {
                labelVal = "Terms & Condition"
                showAboutAppBottomDialogTerms()
            }
            privacyPolicy?.setOnClickListener {
                labelVal = "Privacy Policy"
                showAboutAppBottomDialogTerms()
            }
            logout?.setOnClickListener {
                showLogOutBottomDialog()
            }


        }

        return view
    }

    override fun initVariables() {
    }

    override fun setListeners() {
    }

    override fun setUpBottomNavigationView() {
        TODO("Not yet implemented")
    }

    override fun showLoginSignUp() {
        DataManager(activity as Context).saveBooleanInPreference(PrefKeys.IS_LOGGED_IN, false)
        Preferences.instance?.gender = ""
        Preferences.instance?.savePreferences(activity as Context)
        val intent = Intent(activity, LoginActivity::class.java)
        intent.putExtra("SOURCE", "1")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()

    }

    override fun showErrorMessage() {
        TODO("Not yet implemented")
    }

    override fun registerAppSubmit(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun getAppVersion(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun handleDashboard() {
        TODO("Not yet implemented")
    }

    override fun setUpRankData(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            showMyProfileBottomDialog(jsonObject)
        }
    }

    override fun setUpGameQuestion(jsonObject: JSONObject?) {
    }

    override fun setUpData(jsonObject: JSONObject?) {
        try {
            val listdata = jsonObject!!.getString("data")
            Preferences.instance?.termsAndCondi = listdata
            Preferences.instance?.savePreferences(activity as Context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showSnackBar(message: String?, view: View?) {
    }


    private fun showAboutAppBottomDialog() {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_about_app, null)
            val dialog = BottomSheetDialog(activity as Context)
            val label = dialogView.findViewById<TextView>(R.id.reportErrorLabel)
            val close =
                dialogView.findViewById<ImageView>(R.id.close)
            label.text = "About CL GK Ninja"
            close.setOnClickListener { dialog.cancel() }
            dialog.setContentView(dialogView)
            dialog.window
                ?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog.show()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun showAboutAppBottomDialogTerms() {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_about_app, null)
            val dialog = BottomSheetDialog(activity as Context)
            val label = dialogView.findViewById<TextView>(R.id.reportErrorLabel)
            val description = dialogView.findViewById<TextView>(R.id.description)
            Preferences.instance?.loadPreferences(activity as Context)
            description.text = Preferences.instance?.termsAndCondi?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            val close =
                dialogView.findViewById<ImageView>(R.id.close)
            label.text = labelVal
            close.setOnClickListener { dialog.cancel() }
            dialog.setContentView(dialogView)
            dialog.window
                ?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog.show()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun showMyProfileBottomDialog(jsonObject: JSONObject) {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_my_profile, null)
            val dialog = BottomSheetDialog(activity as Context)
            val profileImage =
                dialogView.findViewById<ImageView>(R.id.image)
            val name = dialogView.findViewById<TextView>(R.id.name)
            val tv_user_name = dialogView.findViewById<TextView>(R.id.tv_user_name)
            val tv_user_dob = dialogView.findViewById<TextView>(R.id.tv_user_dob)
            val tv_user_email = dialogView.findViewById<TextView>(R.id.tv_user_email)
            val tv_user_contact = dialogView.findViewById<TextView>(R.id.tv_user_contact)
            val enrollId = dialogView.findViewById<TextView>(R.id.enrollId)
            val centerName = dialogView.findViewById<TextView>(R.id.centerName)
            val programmes = dialogView.findViewById<TextView>(R.id.programmes)
            val batchName = dialogView.findViewById<TextView>(R.id.batchName)
            var personalJSONOld:JSONObject?=null
            if(jsonObject.has("PERSONAL"))
             personalJSONOld = JSONObject(jsonObject.getString("PERSONAL"))


            val enrollJSON = JSONObject(jsonObject.getString("ENROLLMENT"))
            val personalJSON = personalJSONOld?.getJSONObject("data")
            val dataArray = enrollJSON.getJSONArray("EnrollData")
            val batchArray = dataArray.getJSONObject(0).getJSONArray("batch")
            centerName.text = dataArray.getJSONObject(0).getString("center:")
            programmes.text = dataArray.getJSONObject(0).getString("program")
            enrollId.text = dataArray.getJSONObject(0).getString("enrollIds")
            var bat = ""
            for (i in 0 until batchArray.length()) {
                bat = bat + batchArray.getJSONObject(i).getString("batch") + ","
            }
            if (bat == "") {
            } else {
                val bat2 = bat.substring(0, bat.length - 1)
                batchName.text = bat2
            }
            tv_user_name.text = if (personalJSON?.has("firstName") != false) personalJSON?.getString(
                "firstName"
            ) else ""
            if (personalJSON?.has("middleName") != false) {
                tv_user_name.append(" ")
                tv_user_name.append(personalJSON?.getString("middleName"))
            }
            if (personalJSON?.has("lastName") != false) {
                tv_user_name.append(" ")
                tv_user_name.append(personalJSON?.getString("lastName"))
            }
            name.text = personalJSON?.getString("username")
            //            tvGender.setText(data.getUsername());
            tv_user_dob.setText(
                AppUtils.formatDateTime(
                    personalJSON?.getString("userDateOfBirth"),
                    "yyyy-MM-dd HH:mm:ss.S",
                    "MMM dd, yyyy"
                )
            )
            tv_user_email.text = personalJSON?.getString("email")
            tv_user_contact.text = personalJSON?.getString("mobilePhone")
            val gender = personalJSON?.getString("gender")
            //tvGender.setText(data.getGender());
            if (gender == "Male") Glide.with(activity)
                .load<Any>(R.drawable.male_img)
                .transform(CircleTransform(activity))
                .into(profileImage) else Glide.with(activity)
                .load<Any>(R.drawable.female_img)
                .transform(CircleTransform(activity))
                .into(profileImage)
            dialog.setContentView(dialogView)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showLogOutBottomDialog() {
        try {
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_end_quiz_confirmation, null)
            val dialog = BottomSheetDialog(activity as Context)
            val label1 = dialogView.findViewById<TextView>(R.id.tv_label_submit_test)
            val label2 = dialogView.findViewById<TextView>(R.id.tv_confirmation)
            val button1 =
                dialogView.findViewById<Button>(R.id.btn_submit)
            val button2 =
                dialogView.findViewById<Button>(R.id.btn_go_back)
            label1.text = "Logout from App"
            label2.text = "Are you sure you want to Logout from App?"
            button1.setOnClickListener {
                mHomePresenter?.onLogout()
                dialog.cancel()
            }
            button2.setOnClickListener { dialog.cancel() }
            dialog.setContentView(dialogView)
            dialog.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
            dialog.show()
        } catch (e: java.lang.Exception) {
        }
    }

    fun setGender(gender: String) {
        if (gender == "Male") Glide.with(activity)
            .load<Any>(R.drawable.male_img)
            .transform(CircleTransform(activity))
            .into(profile)
        else Glide.with(activity)
            .load<Any>(R.drawable.female_img)
            .transform(CircleTransform(activity))
            .into(profile)
    }
}