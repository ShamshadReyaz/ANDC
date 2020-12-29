package com.careerlauncher.gkninja.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponseModel {
    /*
            "nextPage": "/endpoints/sisAuthorize/",
            "chage": 24,
            "loginStatus": "VALID",
            "tvAd": "2",
            "tvAdSurvey": "DONE",
            "skip": "1",
            "regSource": "SIS",
            "userType": "STUDENT",
            "message": "old session invalidated, new session created",
            "LastLogin": "WEDNESDAY 29-MAY-2019 14:27:02",
            "prodCat": "studentzone",
            "showDemo": "N",
            "redirectTo": "https://www.aspiration.ai",
            "regId": 348,
            "parentName": "",
            "mobile": ""*/
    @SerializedName("parentId")
    @Expose
    var parentId: String? = null

    @SerializedName("directory")
    @Expose
    var directory: String? = null

    @SerializedName("webUrl")
    @Expose
    var webUrl: String? = null

    @SerializedName("baseUrl")
    @Expose
    var baseUrl: String? = null

    @SerializedName("globalVideoOfflineDays")
    @Expose
    var globalVideoOfflineDays: String? = null

    @SerializedName("simultaneousVideoLimit")
    @Expose
    var simultaneousVideoLimit: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("regId")
    @Expose
    var regId: String? = null

    @SerializedName("redirectTo")
    @Expose
    var redirectTo: String? = null

    @SerializedName("feedbackAd")
    @Expose
    var feedbackAd: String? = null

    @SerializedName("loginStatus")
    @Expose
    var loginStatus: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("tvAd")
    @Expose
    var tvAd: String? = null

    @SerializedName("tvAdSurvey")
    @Expose
    var tvAdSurvey: String? = null

    @SerializedName("userType")
    @Expose
    var userType: String? = null

    @SerializedName("prodCat")
    @Expose
    var prodCat: String? = null

    @SerializedName("underDevelopment")
    @Expose
    var underDevelopment: String? = null

    @SerializedName("underMaintainance")
    @Expose
    var underMaintainance: String? = null

}