package com.mobiquel.dyalsinghapp.data


import com.mobiquel.dyalsinghapp.appinterface.APIInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.apply
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideCertificatePinner() = CertificatePinner.Builder()
        .add(
            "www.dsc.mobiquel.com",
            "sha256/7SX3BrtBGgcILWjEBL1neO6CWTQOyWDycQ7ZzXlkeiU="
        )
        .add(
            "www.dsc.mobiquel.com",
            "sha256/y7xVm0TVJNahMr2sZydE2jQH8SquXV9yLF9seROHHHU="
        )
        .add(
            "www.dsc.mobiquel.com",
            "sha256/sbYXSYat4YdXJf0ksZZGAaUZDjmAKxoEmRYrbssKzDM="
        )
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,certificatePinner: CertificatePinner) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .certificatePinner(certificatePinner)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.dsc.mobiquel.com/api_v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIInterface =
        retrofit.create(APIInterface::class.java)

} 