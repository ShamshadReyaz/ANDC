package com.mobiquel.lehbookingscanner.`interface`

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * Created by Arman Reyaz on 6/16/2021.
 */
interface APIInterface {



    @FormUrlEncoded
    @POST("getAppLatestVersion")
    fun getAppVersion   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("fieldOfficialLogin")
    fun fieldStaffLogin   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getBookingByCode")
    fun getBookingByCodeHeli   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("checkInPassengersForBookingWithoutToken")
    fun checkInPassengersForBookingWithoutToken   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getArmyBookingByCode")
    fun getArmyBookingByCode   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("checkInArmyPassengersForBookingWithoutToken")
    fun checkInArmyPassengersForBookingWithoutToken   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

}