package com.mobiquel.dyalsinghapp.appinterface

import com.mobiquel.dyalsinghapp.pojo.DostToenModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Arman Reyaz on 6/16/2021.
 */
interface APIInterface {
    @FormUrlEncoded
    @POST("studentLoginWithEmail")
    fun studentLogin   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("facultyLoginUpdated")
    fun facultyLoginUpdated   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("verifyLoginOtp")
    fun verifyLoginOtp   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("resendLoginOtp")
    fun resendLoginOtp   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getNotices")
    fun getNotices   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getStudentAttendanceRecord")
    fun getStudentAttendanceRecord   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getStudentAcademicDetailsByRollNo")
    fun getStudentAcademicDetailsByRollNo   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getMessagesForStudentId")
    fun getMessagesForStudentId   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

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
    @POST("getStudentDetailsByRollNo")
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

    @FormUrlEncoded
    @POST("deleteSessionWithAttendance")
    fun deleteSessionWithAttendance   (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("markAttendanceForClassForDates")
    fun markAttendanceForClassForDates (@FieldMap param:MutableMap<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST()
    fun getYourDostToken   (@Url url:String, @Body data:DostToenModel): Call<ResponseBody>


}