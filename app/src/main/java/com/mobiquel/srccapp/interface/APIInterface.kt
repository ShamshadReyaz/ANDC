package com.mobiquel.srccapp.`interface`

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
    @POST("nonTeachingLogin")
    fun nonTeachingLogin   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("studentLogin")
    fun studentLogin   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("facultyLoginUpdated")
    fun facultyLoginUpdated   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getNotices")
    fun getNotices   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getFacultyProfile")
    fun getFacultyProfile   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getStudentDetailsByIdRollNo")
    fun getStudentProfile   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getNonTeachingStaffById")
    fun getNonTeachingStaffById   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>



    @FormUrlEncoded
    @POST("checkSmartProfVersion")
    fun checkSmartProfVersion   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getArmyBookingByCode")
    fun getArmyBookingByCode   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("checkInArmyPassengersForBookingWithoutToken")
    fun checkInArmyPassengersForBookingWithoutToken   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

}