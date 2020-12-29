package com.careerlauncher.gkninja.pojo

import com.careerlauncher.gkninja.network.NetworkConstants

class LoginRequest {
    var username: String? = null
    var password: String? = null
    var rememberMe: String? = null
    var source = "careerlauncher"
    var isCampsis = "yes"
    var redirectTo: String
    var email: String? = null
    var userEmail: String? = null
    var userName: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var deviceType = "Android"

    init {
        redirectTo = NetworkConstants.MAIN_BASE_URL
    }
}