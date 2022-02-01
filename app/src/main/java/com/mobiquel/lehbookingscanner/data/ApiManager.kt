package com.mobiquel.lehbookingscanner.data

import com.mobiquel.lehbookingscanner.`interface`.APIInterface
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
    private val apiClient: APIInterface

    fun getAppVersion(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getAppVersion(param)
    }
    fun fieldStaffLogin(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.fieldStaffLogin(param)
    }
    fun getBookingByCodeHeli(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getBookingByCodeHeli(param)
    }

    fun getBookingByCodeArmy(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getArmyBookingByCode(param)
    }

    fun checkInHeli(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.checkInPassengersForBookingWithoutToken(param)
    }

    fun checkInArmy(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.checkInArmyPassengersForBookingWithoutToken(param)
    }


    companion object {
        var instance: ApiManager? = null
            private set
        var apiBaseUrl = "http:///heliservice.ladakh.gov.in:8080/LadakhHeliPortal/rest/service/"
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

    init {
        apiClient = retrofitService
    }
}