package com.careerlauncher.gkninja.data

import android.content.Context
import com.careerlauncher.gkninja.data.api.ApiManager
import com.careerlauncher.gkninja.data.preferences.PreferenceManager
import com.careerlauncher.gkninja.pojo.*
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Created by Arman Reyaz
 * on 12/17/2020.
 */
class DataManager(context: Context) {
    private val preferenceManager: PreferenceManager? = PreferenceManager.init(context)
    private val apiManager:ApiManager? = ApiManager.init()
    /**
     * method to save value in preferences
     */


    fun saveStringInPreference(key: String?, value: String?) {
        preferenceManager!!.setString(key, value)
    }

    /**
     * method to save value in preferences
     */
    fun saveIntInPreference(key: String?, value: Int) {
        preferenceManager!!.setInt(key, value)
    }

    /**
     * method to save value in preferences
     */
    fun saveBooleanInPreference(key: String?, value: Boolean) {
        preferenceManager!!.setBoolean(key, value)
    }

    /**
     * method to get value from preferences
     */
    fun getStringFromPreference(key: String?): String? {
        return preferenceManager!!.getString(key)
    }

    /**
     * method to get value from preferences
     */
    fun getIntFromPreference(key: String?): Int {
        return preferenceManager!!.getInt(key)
    }

    /**
     * method to get value from preferences
     */
    fun getBooleanFromPreference(key: String?): Boolean {
        return preferenceManager!!.getBoolean(key)
    } // API

    fun login(loginRequest: LoginRequest?): Call<LoginResponseModel>? {
        return apiManager?.login(loginRequest)
    }
    fun sisAuthorize(request: SisAuthorizeRequest?): Call<SisAuthorizeResponseModel>? {
        return apiManager?.sisAuthorize(request)
    }
    fun logout(request: LogoutRequest?): Call<LogoutResponseModel>? {
        return apiManager?.logout(request)
    }
    fun submitFeedback(request: FeedbackRequest?): Call<ResponseBody>? {
        return apiManager?.submitFeedback(request)
    }
    fun registerAppVersion(request: ProdIdUserIdRequest?): Call<ResponseBody>? {
        return apiManager?.registerAppVersion(request)
    }

    fun getRankData(request: UserProdRequest?): Call<ResponseBody>? {
        return apiManager?.getRankData(request)
    }
    fun getTermsAndCondition(request: UserProdRequest?): Call<ResponseBody>? {
        return apiManager?.getTermsAndCondition(request)
    }
    fun getProfileData(request: UserDataRequest?): Call<ResponseBody>? {
        return apiManager?.getProfileData(request)
    }

    fun getEnrolleData(request: UserProdRequest?): Call<ResponseBody>? {
        return apiManager?.getEnrolleData(request)
    }

    fun getGKQuizQuestion(subUrl:String,request: GKQuizRequest?): Call<ResponseBody>? {
        return apiManager?.getGKQuizQuestion(subUrl,request)
    }

    fun sbmitGameQuestion(subUrl:String,request: GKQuizRequest?): Call<ResponseBody>? {
        return apiManager?.submitGameQuestion(subUrl,request)
    }
    fun getAppVersion(request: ProdIdUserIdRequest?): Call<ResponseBody>? {
        return apiManager?.getAppVersion(request)
    }

    companion object {
        var instance: DataManager? = null
            private set

        @Synchronized
        fun init(context: Context) {
            if (instance == null) {
                instance = DataManager(context)
            }
        }
    }

}