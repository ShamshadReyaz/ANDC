package com.mobiquel.srccapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.pojo.ProfileRequestModel
import com.mobiquel.srccapp.utils.SingleLiveEvent
import com.mobiquel.srccapp.view.repo.APIRepository
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