package com.careerlauncher.gkninja.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentTransaction
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseActivity
import com.careerlauncher.gkninja.dialogs.UpdateAppDialog
import com.careerlauncher.gkninja.interfaces.DialogListener
import com.careerlauncher.gkninja.pojo.FailureResponse
import com.careerlauncher.gkninja.utils.*
import com.careerlauncher.gkninja.utils.service.SoundService
import com.careerlauncher.gkninja.view.home.fragments.DashboardFragment
import com.careerlauncher.gkninja.view.home.fragments.GameFragment
import com.careerlauncher.gkninja.view.home.fragments.NotificationFragment
import com.careerlauncher.gkninja.view.home.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class HomeActivity : BaseActivity(), HomeView {

    private var versioName: String = ""
    lateinit var dashboardFragment: DashboardFragment
    lateinit var gameFragment: GameFragment
    lateinit var notificationFragment: NotificationFragment
    lateinit var settingsFragment: SettingsFragment
    var doubleBackToExitPressedOnce: Boolean = false
    lateinit var mUpdateAppDialo: UpdateAppDialog
    var mPresenter: HomePresenter? = null
    var bottomNavigation: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.menu?.findItem(R.id.navigation_home)?.setChecked(true)

        mPresenter = HomePresenter(this)

       /* gameFragment = GameFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container_home, gameFragment)
            .commit()
*/
        versioName = getAppVersion()
        val deiceId = getDeviceId()
        mPresenter!!.registerAppVersion(versioName, versioName, deiceId)

       /* bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    dashboardFragment = DashboardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, dashboardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_home -> {
                    gameFragment = GameFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, gameFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_notifications -> {
                    notificationFragment = NotificationFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, notificationFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_settings -> {
                    settingsFragment = SettingsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, settingsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }
       */
        bottomNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            val fragment = GameFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fl_container_home, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
        Preferences.instance?.loadPreferences(this@HomeActivity)

        if (Preferences.instance?.gender.equals(""))
            mPresenter?.getProfileData_1()

        if (Preferences.instance?.gameSound.equals("1"))
            startService(Intent(baseContext, SoundService::class.java))

    }

    override fun initVariables() {
        mUpdateAppDialo = UpdateAppDialog(this, object : DialogListener {
            override fun onPositiveButtonClick() {
                mUpdateAppDialo.cancel()
                val url =
                    "https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                startActivity(Intent("android.intent.action.VIEW", Uri.parse(url)))
            }

            override fun onNegativeButtonClick() {
                mUpdateAppDialo.cancel()
            }

        })
    }

    override fun setListeners() {
    }

    override val resourceId: Int
        get() = R.layout.activity_home

    override fun setUpBottomNavigationView() {
        TODO("Not yet implemented")
    }

    override fun showLoginSignUp() {
        TODO("Not yet implemented")
    }

    override fun showErrorMessage() {

    }

    override fun registerAppSubmit(jsonObject: JSONObject?) {
        val deiceId = getDeviceId()
        mPresenter!!.getAppVersion(versioName, versioName, deiceId)
    }

    override fun getAppVersion(jsonObject: JSONObject?) {
        if (!isFinishing) {
            val versioFromServer = jsonObject?.getString("latestUpdateVersionNumber")
            val aboutUpdate = ""
            if (!versioName.equals(versioFromServer)) {
                if (jsonObject?.getString("forcefullyUpdate") == "true") {
                    mUpdateAppDialo?.show()
                    mUpdateAppDialo?.setCancelable(false)
                    mUpdateAppDialo?.setButtonName("Update")
                    mUpdateAppDialo?.setAppUpdateData(
                        "New update '" + jsonObject?.getString("latestUpdateVersionNumber") + "' is available! It is mandatory to update your app.",
                        aboutUpdate
                    )
                    mUpdateAppDialo?.hideCheckBox()
                } else {
                    mUpdateAppDialo?.show()
                    mUpdateAppDialo?.setButtonName("Update")
                    mUpdateAppDialo?.hideCheckBox()
                    mUpdateAppDialo?.setAppUpdateData(
                        "New update '" + jsonObject?.getString("latestUpdateVersionNumber") + "' is available! It is recommended to update your app.",
                        aboutUpdate
                    )
                }
            }
        }
    }

    override fun handleDashboard() {

    }

    override fun setUpRankData(jsonObject: JSONObject?) {
        try {
            val gender = jsonObject!!.getJSONObject("data").getString("gender")
            Preferences.instance?.gender = gender
            Preferences.instance?.savePreferences(this@HomeActivity)
            val fragment = SettingsFragment()
            fragment.setGender(gender)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setUpGameQuestion(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun setUpData(jsonObject: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun showSpecificError(failureResponse: FailureResponse?) {
        showToast(AppConstants.ERROR_LOADING_DATA)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        showToast("Press back once again to exit")
        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                doubleBackToExitPressedOnce = false
            }
        }, 2000)

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_dashboard -> {
                    val fragment = DashboardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_home -> {
                    val fragment = GameFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    val fragment = NotificationFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    val fragment = SettingsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_container_home, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }
}