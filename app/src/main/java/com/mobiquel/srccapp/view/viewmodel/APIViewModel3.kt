package com.mobiquel.srccapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.pojo.ProfileRequestModel
import com.mobiquel.srccapp.utils.SingleLiveEvent
import com.mobiquel.srccapp.view.repo.APIRepository
import okhttp3.ResponseBody

class APIViewModel3 : ViewModel() {

    var uploadData = SingleLiveEvent<Resource<ResponseBody>>()


    fun getMaintenance(
        userId: String,
        userType: String
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getMaintenanceRequests(userId,userType)
        return uploadData
    }

}