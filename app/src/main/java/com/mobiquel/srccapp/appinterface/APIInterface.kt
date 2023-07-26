package com.mobiquel.srccapp.appinterface

import com.mobiquel.srccapp.pojo.DostToenModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Arman Reyaz on 6/16/2021.
 */
interface APIInterface {




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
    @POST("getMyRequisitionsForMaintenance")
    fun getMyRequisitionsForMaintenance   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getStudentWiFiCredentialRecord")
    fun getStudentWiFiCredentialRecord   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getMSTeamsPasswordForEmail")
    fun getMSTeamsPasswordForEmail   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("addRequisitionForMaintenance")
    fun addRequisitionForMaintenance   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("getStudentDetailsByIdRollNo")
    fun getStudentProfile   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getNonTeachingStaffById")
    fun getNonTeachingStaffById   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("updateUserMobilePersonalEmail")
    fun updateUserMobilePersonalEmail   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("checkSmartProfVersion")
    fun checkSmartProfVersion   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getVirtualClassForFaculty")
    fun getVirtualClassForFaculty   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getPapersForFacultyByVirtualGroup")
    fun getPapersForFacultyByVirtualGroup   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getVirtualClassForAttendanceForDates")
    fun getVirtualClassForAttendanceForDates   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST()
    fun getYourDostToken   (@Url url:String, @Body data:DostToenModel): Call<ResponseBody>


}