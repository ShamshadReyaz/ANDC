package com.careerlauncher.gkninja.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationResponseModel {
    @SerializedName("parentId")
    @Expose
    var parentId: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("registrationStatus")
    @Expose
    var registrationStatus: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("regId")
    @Expose
    var regId: String? = null

    @SerializedName("userType")
    @Expose
    var userType: String? = null

}