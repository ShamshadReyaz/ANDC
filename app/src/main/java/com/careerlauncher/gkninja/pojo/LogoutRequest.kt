package com.careerlauncher.gkninja.pojo

import com.careerlauncher.gkninja.network.NetworkConstants

class LogoutRequest {
    var userId: String? = null
    var prodId: String? = null
    var prodCat: String? = null
    var source = "careerlauncher"
    var isCampsis = "yes"
    var redirectTo: String
    var deviceType = "Android"

    init {
        redirectTo = NetworkConstants.MAIN_BASE_URL
    }
}