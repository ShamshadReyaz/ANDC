package com.careerlauncher.gkninja.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LogoutResponseModel {
    @SerializedName("logoutStatus")
    @Expose
    var logoutStatus: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

}