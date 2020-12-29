package com.careerlauncher.gkninja.network

import com.careerlauncher.gkninja.pojo.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class NetworkResponse<T>(private val handler: CommonResponseHandler?) :
    Callback<T?> {
    abstract fun onSuccess(body: T?)
    abstract fun onFailure(code: Int, failureResponse: FailureResponse?)
    abstract fun onError(t: Throwable?)
    override fun onResponse(
        call: Call<T?>,
        response: Response<T?>
    ) {
        if (response.body() != null && response.isSuccessful) {
            onSuccess(response.body())
        } else {
            onFailure(response.code(), getFailureErrorBody(response))
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        if (t is SocketTimeoutException || t is UnknownHostException) {
            handler?.onNetworkError()
        }
        onError(t)
    }

    /**
     * Create your custom failure response out of server response
     */
    private fun getFailureErrorBody(errorBody: Response<T?>): FailureResponse {
        val baseResponse = FailureResponse()
        baseResponse.message = errorBody.message()
        baseResponse.code = errorBody.code()
        return baseResponse
    }

}