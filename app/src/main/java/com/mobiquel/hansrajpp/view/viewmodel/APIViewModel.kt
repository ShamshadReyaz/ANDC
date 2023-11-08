package com.mobiquel.hansrajpp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.hansrajpp.pojo.CheckVersionModel
import com.mobiquel.hansrajpp.pojo.ProfileRequestModel
import com.mobiquel.hansrajpp.utils.SingleLiveEvent
import com.mobiquel.hansrajpp.view.repo.APIRepository
import okhttp3.ResponseBody

class APIViewModel : ViewModel() {

    var uploadData = SingleLiveEvent<Resource<ResponseBody>>()

    fun login(
        userType: String,
        email: String,
        pwd: String
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.login(userType, email, pwd)
        return uploadData
    }

    fun getNotices(
        email: String, userType: String
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getNotices(email, userType)
        return uploadData
    }

    fun getStudentAttendance(
        studentId: String
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getStudentAttendance(studentId)
        return uploadData
    }



    fun getProfile(
        model: ProfileRequestModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getProfile(model)
        return uploadData
    }

    fun checkSmartProfVersion(
        model: CheckVersionModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.checkSmartProfVersion(model)
        return uploadData
    }


}