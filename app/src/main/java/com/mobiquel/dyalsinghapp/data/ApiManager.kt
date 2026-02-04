package com.mobiquel.dyalsinghapp.data

import com.mobiquel.dyalsinghapp.appinterface.APIInterface
import com.mobiquel.dyalsinghapp.pojo.DostToenModel
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject


class ApiManager  @Inject constructor() {

    @Inject
    lateinit var apiClient: APIInterface

    fun facultyLogin(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.facultyLoginUpdated(param)
    }
    fun verifyLoginOtp(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.verifyLoginOtp(param)
    }
    fun resendLoginOtp(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.resendLoginOtp(param)
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

    fun getStudentAttendanceRecord(param:MutableMap<String, String>): Call<ResponseBody> {
        return apiClient.getStudentAttendanceRecord(param)
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




}