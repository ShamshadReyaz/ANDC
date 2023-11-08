package com.mobiquel.hansrajpp.data

import com.mobiquel.hansrajpp.appinterface.APIInterface
import com.mobiquel.hansrajpp.pojo.DostToenModel
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Navjot Singh
 * on 2/3/19.
 */
class ApiManager private constructor() {
    private val apiClient: APIInterface = retrofitService

    fun nonTeachingLogin(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.nonTeachingLogin(param)
    }

    fun facultyLogin(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.facultyLoginUpdated(param)
    }


    fun studentLogin(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.studentLogin(param)
    }

    fun getNotices(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getNotices(param)
    }
    fun getMyRequisitionsForMaintenance(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getMyRequisitionsForMaintenance(param)
    }

    fun addRequisitionForMaintenance(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.addRequisitionForMaintenance(param)
    }

    fun getFacultyProfile(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getFacultyProfile(param)
    }

    fun getStudentProfile(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getStudentProfile(param)
    }

    fun getNonTeachingStaffById(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getNonTeachingStaffById(param)
    }

    fun updateUserMobilePersonalEmail(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.updateUserMobilePersonalEmail(param)
    }



    fun checkSmartProfVersion(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.checkSmartProfVersion(param)
    }
    fun getStudentAcademicDetailsByRollNo(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getStudentAcademicDetailsByRollNo(param)
    }
    fun getMessagesForStudentId(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getMessagesForStudentId(param)
    }

    fun getVirtualClassForFaculty(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getVirtualClassForFaculty(param)
    }

    fun getPapersForFacultyByVirtualGroup(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getPapersForFacultyByVirtualGroup(param)
    }


    fun getVirtualClassForAttendanceForDates(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getVirtualClassForAttendanceForDates(param)
    }
    fun deleteSessionWithAttendance(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.deleteSessionWithAttendance(param)
    }
    fun markAttendanceForClassForDates(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.markAttendanceForClassForDates(param)
    }



    fun getYourDostToken(param:DostToenModel): Call<ResponseBody> {
        return apiClient.getYourDostToken("https://yourdost.com/zion/v2/users/sso/token",param)
    }


    companion object {
        var instance: ApiManager? = null
            private set
        var apiBaseUrl = "http://206.189.138.254:8080/HansrajSmartProf/rest/service/"
        private var retrofit: Retrofit? = null
        private var client: OkHttpClient? = null
        fun init(): ApiManager? {
            if (instance == null) {
                synchronized(ApiManager::class.java) { if (instance == null) instance = ApiManager() }
            }
            return instance
        }

        private val retrofitService: APIInterface
            private get() {
                val logging = HttpLoggingInterceptor()
                // set your desired log level
                logging.level = HttpLoggingInterceptor.Level.BODY
                client = OkHttpClient.Builder()
                        .connectTimeout(5000, TimeUnit.SECONDS)
                        .addInterceptor(logging)
                        .readTimeout(5000, TimeUnit.SECONDS).build()
                retrofit = Retrofit.Builder()
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(apiBaseUrl)
                        .build()
                return retrofit?.create(APIInterface::class.java)!!
            }

        fun changeApiBaseUrl(newApiBaseUrl: String) {
            apiBaseUrl = newApiBaseUrl
            retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(apiBaseUrl).build()
        }


    }

}