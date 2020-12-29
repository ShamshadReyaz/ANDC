package com.careerlauncher.gkninja.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SisAuthorizeResponseModel {
    /*
   {
    "stream": "",
    "class_id": "",
    "status": "success",
    "prodId": 0,
    "homeUrl": "/studentzone/index.jsp",
    "ASPuserFlag": "NO",
    "loggedInSIS": "studentzone",
    "redirectTo": "",
    "AISIS": "AISIS"
}*/
    @SerializedName("stream")
    @Expose
    var stream: String? = null

    @SerializedName("directory")
    @Expose
    var directory: String? = null

    @SerializedName("serviceLocation")
    @Expose
    var serviceLocation: String? = null

    @SerializedName("class_id")
    @Expose
    var class_id: String? = null

    @SerializedName("PRODTYPE")
    @Expose
    var prodType: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("testGym")
    @Expose
    var testGym: String? = null

    @SerializedName("prodId")
    @Expose
    var prodId: String? = null

    @SerializedName("homeUrl")
    @Expose
    var homeUrl: String? = null

    @SerializedName("externalURLCheck")
    @Expose
    var externalURLCheck: String? = null

    @SerializedName("ASPuserFlag")
    @Expose
    var aSPuserFlag: String? = null

    @SerializedName("demoFlag")
    @Expose
    var demoFlag: String? = null

    @SerializedName("loggedInSIS")
    @Expose
    var loggedInSIS: String? = null

    @SerializedName("redirectTo")
    @Expose
    var redirectTo: String? = null

    @SerializedName("AISIS")
    @Expose
    var aISIS: String? = null

}