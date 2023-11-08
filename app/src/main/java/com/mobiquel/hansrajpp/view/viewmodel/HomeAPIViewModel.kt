package com.mobiquel.hansrajpp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.hansrajpp.pojo.CheckVersionModel
import com.mobiquel.hansrajpp.utils.SingleLiveEvent
import com.mobiquel.hansrajpp.view.repo.APIRepository
import okhttp3.ResponseBody

class HomeAPIViewModel : ViewModel() {

    var uploadData = SingleLiveEvent<Resource<ResponseBody>>()



    fun checkSmartProfVersion(
        model: CheckVersionModel
    ): SingleLiveEvent<Resource<ResponseBody>>? {
        uploadData = APIRepository.checkSmartProfVersion(model)
        return uploadData
    }


}