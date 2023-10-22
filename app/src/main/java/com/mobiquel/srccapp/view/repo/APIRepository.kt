package com.mobiquel.srccapp.view.repo

import androidx.lifecycle.MutableLiveData
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.srccapp.data.RetrofitClient
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.pojo.ProfileRequestModel
import com.mobiquel.srccapp.utils.SingleLiveEvent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Arman Reyaz on 7/1/2021.
 */
object APIRepository {

    var serviceSetterGetterNew = MutableLiveData<Resource<ResponseBody>>()
    var serviceSetterGetter = SingleLiveEvent<Resource<ResponseBody>>()
    var serviceSetterGetter1 = MutableLiveData<Resource<ResponseBody>>()
    var serviceSetterGetter2 = MutableLiveData<Resource<ResponseBody>>()

    fun getNotices(email: String, userType: String): SingleLiveEvent<Resource<ResponseBody>> {


        val data: MutableMap<String, String> = HashMap()
        data["email"] = email
        data["userType"] = userType

        val call = RetrofitClient.providesApiService.getNotices(data)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter
    }
    fun getStudentAttendance(studentId: String): SingleLiveEvent<Resource<ResponseBody>> {


        val data: MutableMap<String, String> = HashMap()
        data["studentId"] = studentId

        val call = RetrofitClient.providesApiService.getStudentAttendanceRecord(data)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter
    }

    fun login(
        userType: String,
        email: String,
        pwd: String
    ): SingleLiveEvent<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["email"] = email
        data["password"] = pwd
        var call: Call<ResponseBody>? = null
        if (userType.equals("faculty"))
            call = RetrofitClient.providesApiService.facultyLoginUpdated(data)
        else if (userType.equals("student"))
            call = RetrofitClient.providesApiService.studentLogin(data)
        else
            call = RetrofitClient.providesApiService.nonTeachingLogin(data)

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }

        })
        return this.serviceSetterGetter
    }

    fun getProfile(model: ProfileRequestModel): SingleLiveEvent<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["staffId"] = model.staffId!!
        data["studentId"] = model.studentId!!
        data["collegeRollNo"] = model.collegeRollNo!!
        data["facultyId"] = model.facultyId!!
        var call: Call<ResponseBody>? = null

        if (model.userType!!.equals("faculty"))
            call = RetrofitClient.providesApiService.getFacultyProfile(data)
        else if (model.userType!!.equals("student"))
            call = RetrofitClient.providesApiService.getStudentProfile(data)
        else
            call = RetrofitClient.providesApiService.getNonTeachingStaffById(data)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter
    }

    fun getMaintenanceRequests(
        userId: String,
        userType: String
    ): SingleLiveEvent<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["userId"] = userId
        data["userType"] = userType
        var call: Call<ResponseBody>? = null
        call = RetrofitClient.providesApiService.getMyRequisitionsForMaintenance(data)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter
    }

    fun getWifiData(
        rollNUmber: String
    ): MutableLiveData<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["rollNo"] = rollNUmber
        var call: Call<ResponseBody>? = null
        call = RetrofitClient.providesApiService.getStudentWiFiCredentialRecord(data)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter1.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter1.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter1.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter1
    }
    fun getMsData(
        email: String
    ): MutableLiveData<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["email"] = email
        var call: Call<ResponseBody>? = null
        call = RetrofitClient.providesApiService.getMSTeamsPasswordForEmail(data)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter2.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter2.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter2.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter2
    }

    fun checkSmartProfVersion(model: CheckVersionModel): SingleLiveEvent<Resource<ResponseBody>> {

        val data: MutableMap<String, String> = HashMap()
        data["os"] = model.os!!
        data["userType"] = model.userType!!
        data["userId"] = model.userId!!
        data["pushNotificationId"] = model.pushNotificationId!!
        var call: Call<ResponseBody>? = null

        call = RetrofitClient.providesApiService.checkSmartProfVersion(data)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful)
                    this@APIRepository.serviceSetterGetter.value = Resource.success(response.body())
                else
                    this@APIRepository.serviceSetterGetter.value =
                        Resource.error("Something Went Wrong! Please try again later", null)


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                this@APIRepository.serviceSetterGetter.value =
                    Resource.error("Internet not available! Please try again later", null)
            }


        })
        return this.serviceSetterGetter
    }

}