package com.mobiquel.dyalsinghapp.data

class FailureResponse {
    var code = 0
    var message: String? = null

    constructor() {}
    constructor(code: Int, message: String?) {
        this.code = code
        this.message = message
    }

}