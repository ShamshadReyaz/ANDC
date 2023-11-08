package com.mobiquel.hansrajpp.data

import com.mobiquel.hansrajpp.appinterface.APIInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofitClient: Retrofit.Builder by lazy {

        val levelType = HttpLoggingInterceptor.Level.BODY
        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)
        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl("http://139.59.93.96:8080/RollCallSRCC/rest/service/")
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val providesApiService: APIInterface by lazy {
        retrofitClient
            .build()
            .create(APIInterface::class.java)
    }
}