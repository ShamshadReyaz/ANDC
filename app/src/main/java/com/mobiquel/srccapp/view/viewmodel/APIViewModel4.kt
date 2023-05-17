package com.mobiquel.srccapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobiquel.lehpermit.data.Resource
import com.mobiquel.srccapp.pojo.CheckVersionModel
import com.mobiquel.srccapp.pojo.ProfileRequestModel
import com.mobiquel.srccapp.utils.SingleLiveEvent
import com.mobiquel.srccapp.view.repo.APIRepository
import okhttp3.ResponseBody

class APIViewModel4 : ViewModel() {

    var uploadData = MutableLiveData<Resource<ResponseBody>>()
    var uploadData2 = MutableLiveData<Resource<ResponseBody>>()


    fun getWifiData(
        rollNo: String
    ) {
        uploadData = APIRepository.getWifiData(rollNo)
        //return uploadData
    }

    fun getMsData(
        email: String
    ) {
        uploadData2 = APIRepository.getMsData(email)
        //return uploadData2
    }

    fun returnMsData(): MutableLiveData<Resource<ResponseBody>> {
        return uploadData2
    }

    fun returnWifiData(): MutableLiveData<Resource<ResponseBody>> {
        return uploadData
    }

}