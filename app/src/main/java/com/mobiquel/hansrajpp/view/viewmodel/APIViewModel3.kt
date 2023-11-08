package com.mobiquel.hansrajpp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.hansrajpp.utils.SingleLiveEvent
import com.mobiquel.hansrajpp.view.repo.APIRepository
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