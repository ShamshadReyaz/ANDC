package com.mobiquel.hansrajpp.pojo

import com.google.gson.annotations.SerializedName

data class DostToenModel (

    @SerializedName("email")
    var email: String? = null,
    @SerializedName("organizationID")
    var organizationID: Int? = null

)