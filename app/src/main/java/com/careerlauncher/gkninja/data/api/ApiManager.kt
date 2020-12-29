package com.careerlauncher.gkninja.data.api

import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.pojo.*
import com.careerlauncher.gkninja.pojo.LoginResponseModel
import com.careerlauncher.gkninja.pojo.LogoutResponseModel
import com.careerlauncher.gkninja.pojo.RegistrationResponseModel
import com.careerlauncher.gkninja.pojo.SisAuthorizeResponseModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
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
    private val apiClient: ApiClient
    fun login(loginRequest: LoginRequest?): Call<LoginResponseModel> {
        return apiClient.login(loginRequest)
    }
    fun sisAuthorize(request: SisAuthorizeRequest?): Call<SisAuthorizeResponseModel> {
        return apiClient.sisAuthorize(request)
    }
    fun logout(request: LogoutRequest?): Call<LogoutResponseModel> {
        return apiClient.logout(request)
    }
    fun submitFeedback(request: FeedbackRequest?): Call<ResponseBody> {
        return apiClient.submitFeedback(request)
    }
    fun registerAppVersion(request: ProdIdUserIdRequest?): Call<ResponseBody> {
        return apiClient.registerAppVersion(request)
    }
    fun getRankData(request: UserProdRequest?): Call<ResponseBody> {
        return apiClient.getRankData(request)
    }
    fun getProfileData(request: UserDataRequest?): Call<ResponseBody> {
        return apiClient.getProfileData(request)
    }

    fun getEnrolleData(request: UserProdRequest?): Call<ResponseBody> {
        return apiClient.getEnrolleData("MBA",request)
    }
    fun getTermsAndCondition(request: UserProdRequest?): Call<ResponseBody> {
        return apiClient.getTermsAndCondition(request)
    }


    fun getGKQuizQuestion(subUrl:String,request: GKQuizRequest?): Call<ResponseBody> {
        return apiClient.getGKQuizQuestionDynamic(subUrl,request)
    }

    fun submitGameQuestion(subUrl:String,request: GKQuizRequest?): Call<ResponseBody> {
        return apiClient.submitGameQuestionDynamic(subUrl,request)
    }

    fun getAppVersion(request: ProdIdUserIdRequest?): Call<ResponseBody> {
        return apiClient.getAppVersion(request)
    }


    companion object {
        var instance: ApiManager? = null
            private set
        var apiBaseUrl = "https://www.aspiration.link/endpoints/"
        private var retrofit: Retrofit? = null
        private var client: OkHttpClient? = null
        fun init(): ApiManager? {
            if (instance == null) {
                synchronized(ApiManager::class.java) { if (instance == null) instance = ApiManager() }
            }
            return instance
        }

        // set your desired log level
        private val retrofitService: ApiClient
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
                return retrofit?.create(ApiClient::class.java)!!
            }

        fun changeApiBaseUrl(newApiBaseUrl: String) {
            apiBaseUrl = newApiBaseUrl
            retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(apiBaseUrl).build()
        }

        private val okHttpClient: OkHttpClient
            private get() {
                val httpClientBuilder = OkHttpClient.Builder()
                httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.addNetworkInterceptor { chain ->
                    val request = chain.request()
                    val requestBuilder = request.newBuilder()
                            .method(request.method(), request.body())
                    chain.proceed(requestBuilder.build())
                }
                return httpClientBuilder.build()
            }
    }

    init {
        apiClient = retrofitService
    }
}