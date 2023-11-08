package com.mobiquel.hansrajpp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.hansrajpp.pojo.ProfileRequestModel
import com.mobiquel.hansrajpp.utils.SingleLiveEvent
import com.mobiquel.hansrajpp.view.repo.APIRepository
import okhttp3.ResponseBody

class APIViewModel2 : ViewModel() {

    var uploadData = SingleLiveEvent<Resource<ResponseBody>>()


    fun getProfile(
        model: ProfileRequestModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getProfile(model)
        return uploadData
    }
    fun getWifiData(
        model: ProfileRequestModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.getProfile(model)
        return uploadData
    }

}