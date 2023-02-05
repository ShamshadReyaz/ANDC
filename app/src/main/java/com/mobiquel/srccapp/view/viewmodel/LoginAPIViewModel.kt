package com.mobiquel.srccapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.pojo.ProfileRequestModel
import com.mobiquel.srccapp.utils.SingleLiveEvent
import com.mobiquel.srccapp.view.repo.APIRepository
import okhttp3.ResponseBody

class LoginAPIViewModel : ViewModel() {

    var uploadData = SingleLiveEvent<Resource<ResponseBody>>()

    fun login(
        userType: String,
        email: String,
        pwd: String
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.login(userType, email, pwd)
        return uploadData
    }


    fun checkSmartProfVersion(
        model: CheckVersionModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.checkSmartProfVersion(model)
        return uploadData
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("CLEARED","VIEWMDOEL")
    }


}