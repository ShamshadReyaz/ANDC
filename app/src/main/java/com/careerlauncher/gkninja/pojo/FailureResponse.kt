package com.careerlauncher.gkninja.pojo

class FailureResponse {
    var code = 0
    var message: String? = null

    constructor() {}
    constructor(code: Int, message: String?) {
        this.code = code
        this.message = message
    }

}