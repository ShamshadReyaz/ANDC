package com.careerlauncher.gkninja.data.api

import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.pojo.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Arman Reyaz on 12/18/2020.
 */
interface ApiClient {

    @POST(NetworkConstants.END_POINT_LOGIN)
    fun login(@Body loginRequest: LoginRequest?): Call<LoginResponseModel>

    @POST(NetworkConstants.END_POINT_SIS_AUTHORIZE)
    fun sisAuthorize(@Body sisAuthorizeRequest: SisAuthorizeRequest?): Call<SisAuthorizeResponseModel>

    @POST(NetworkConstants.END_POINT_LOGOUT)
    fun logout(@Body sisAuthorizeRequest: LogoutRequest?): Call<LogoutResponseModel>

    @POST(NetworkConstants.END_POINT_INSERT_FEEDBACK)
    fun submitFeedback(@Body sisAuthorizeRequest: FeedbackRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_VERSION_REGISTER)
    fun registerAppVersion(@Body sisAuthorizeRequest: ProdIdUserIdRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_GK_QSET_TODAY_YESTERDAY)
    fun getRankData(@Body sisAuthorizeRequest: UserProdRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_USER_DETAILS)
    fun getProfileData(@Body sisAuthorizeRequest: UserDataRequest?): Call<ResponseBody>

    @POST("{prodCat}"+NetworkConstants.END_POINT_PERSONAL_DETAIL)
    fun getEnrolleData(@Path(value = "prodCat",encoded = true) prodCat: String?,@Body sisAuthorizeRequest: UserProdRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_PRIVACY_POLICY)
    fun getTermsAndCondition(@Body sisAuthorizeRequest: UserProdRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_GK_GAME)
    fun getGKQuizQuestion(@Body sisAuthorizeRequest: GKQuizRequest?): Call<ResponseBody>

    @POST("{subUrl}" )
    fun getGKQuizQuestionDynamic(@Path(value = "subUrl",encoded = true) prodCat: String?, @Body sisAuthorizeRequest: GKQuizRequest?): Call<ResponseBody>

    @POST("{subUrl}" )
    fun submitGameQuestionDynamic(@Path(value = "subUrl",encoded = true) prodCat: String?, @Body sisAuthorizeRequest: GKQuizRequest?): Call<ResponseBody>

    @POST(NetworkConstants.END_POINT_GET_APP_VERSION)
    fun getAppVersion(@Body sisAuthorizeRequest: ProdIdUserIdRequest?): Call<ResponseBody>


}