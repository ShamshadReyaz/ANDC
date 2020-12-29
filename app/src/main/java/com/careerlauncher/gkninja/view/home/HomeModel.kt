package com.careerlauncher.gkninja.view.home

import com.careerlauncher.gkninja.base.BaseModel
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.network.NetworkResponse
import com.careerlauncher.gkninja.pojo.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class HomeModel(listener: HomeModelListener?) :
    BaseModel<HomeModelListener?>(listener) {
    override fun init() {}
    fun logout() {
        val logoutRequest = LogoutRequest()
        logoutRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        dataManager.logout(logoutRequest)?.enqueue(object :
            NetworkResponse<LogoutResponseModel?>(this) {
            override fun onSuccess(body: LogoutResponseModel?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        if (body.logoutStatus == "success") dataManager.saveBooleanInPreference(
                            PrefKeys.IS_LOGGED_IN,
                            false
                        )
                        getListener()!!.handleLogoutResult(body)
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }


    fun submitFeedback(data: List<String?>) {
        val logoutRequest = FeedbackRequest()
        logoutRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        logoutRequest.feedbackType = data[2]
        logoutRequest.feedbackDetail = data[0]
        logoutRequest.appType = "mobile-android"
        logoutRequest.appVersion = data[3]
        logoutRequest.rating = data[1]
        logoutRequest.remarks = data[0]
        dataManager.submitFeedback(logoutRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleSubmiResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }
    fun getAppVersion(versionName: String,versionNo: String,deviceId:String) {
        val registerAppRequest = ProdIdUserIdRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        registerAppRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        registerAppRequest.deviceId = deviceId
        registerAppRequest.latitude=""
        registerAppRequest.longitude=""
        dataManager.getAppVersion(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleGetAppVersionResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

    fun registerAppVersion(versionName: String,versionNo: String,deviceId:String) {
        val registerAppRequest = ProdIdUserIdRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        registerAppRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        registerAppRequest.appName = "Careerlauncher"
        registerAppRequest.versionNumber = versionNo
        registerAppRequest.versionName = versionName
        registerAppRequest.deviceId = deviceId
        dataManager.registerAppVersion(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleRegisterAppResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }
    fun getRankData() {
        val registerAppRequest = UserProdRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        registerAppRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        registerAppRequest.demoFlag = dataManager.getStringFromPreference(PrefKeys.DEMO_FLAG)

        dataManager.getRankData(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleRankResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

    fun getTermsAndCondition() {
        val registerAppRequest = UserProdRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        registerAppRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)

        dataManager.getTermsAndCondition(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleTermsConditionResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

    fun getProfileData_1() {
        val registerAppRequest = UserDataRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.username = dataManager.getStringFromPreference(PrefKeys.USER_NAME)
        registerAppRequest.password = dataManager.getStringFromPreference(PrefKeys.USER_PASSWORD)

        dataManager.getProfileData(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val jsonObject=JSONObject(jsonStr)
                            getListener()!!.handleRankResponse(jsonObject)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

    fun getProfileData() {
        val registerAppRequest = UserDataRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.username = dataManager.getStringFromPreference(PrefKeys.USER_NAME)
        registerAppRequest.password = dataManager.getStringFromPreference(PrefKeys.USER_PASSWORD)

        dataManager.getProfileData(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            getEnrollmentData(jsonStr)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }
    fun getEnrollmentData(jsonString:String) {
        val registerAppRequest = UserProdRequest()
        registerAppRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        registerAppRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        registerAppRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)

        dataManager.getEnrolleData(registerAppRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject()
                            `object`.put("PERSONAL",jsonString)
                            `object`.put("ENROLLMENT",jsonStr)
                            getListener()!!.handleRankResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

    fun getGKQuizQuestion(testStatus:String, type:String, seqNo:String, qsetId:String,gameType:String) {
        val quizRequest = GKQuizRequest()
        quizRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        quizRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        quizRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        quizRequest.test=testStatus
        quizRequest.typ=type
        quizRequest.seqNo=seqNo
        quizRequest.qsetId=qsetId
        var subUrl=""
        if(gameType.equals("GK"))
            subUrl=NetworkConstants.END_POINT_GK_GAME
        else if(gameType.equals("FACTS"))
            subUrl=NetworkConstants.END_POINT_FACTS_GAME
        else if(gameType.equals("LEGAL"))
            subUrl=NetworkConstants.END_POINT_LEGAL_GAME
        else if(gameType.equals("VOCAB"))
            subUrl=NetworkConstants.END_POINT_VOCAB_GAME

        dataManager.getGKQuizQuestion(subUrl,quizRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleQuizResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }
    fun submitGameQuestion(success:String, seqNo:String, qsetId:String,gameType: String) {
        val quizRequest = GKQuizRequest()
        quizRequest.userId = dataManager.getStringFromPreference(PrefKeys.USER_ID)
        quizRequest.prodId = dataManager.getStringFromPreference(PrefKeys.PROD_ID)
        quizRequest.prodCat = dataManager.getStringFromPreference(PrefKeys.PROD_CAT)
        quizRequest.success=success
        quizRequest.seqNo=seqNo
        quizRequest.qsetId=qsetId
        var subUrl=""
        if(gameType.equals("GK"))
            subUrl=NetworkConstants.END_POINT_SUBMIT_GK_GAME
        else if(gameType.equals("FACTS"))
            subUrl=NetworkConstants.END_POINT_SUBMIT_FACTS_GAME
        else if(gameType.equals("LEGAL"))
            subUrl=NetworkConstants.END_POINT_SUBMIT_LEGAL_GAME
        else if(gameType.equals("VOCAB"))
            subUrl=NetworkConstants.END_POINT_SUBMIT_VOCAB_GAME

        dataManager.sbmitGameQuestion(subUrl,quizRequest)?.enqueue(object :
            NetworkResponse<ResponseBody?>(this) {
            override fun onSuccess(body: ResponseBody?) {
                if (getListener() != null) {
                    if (body != null) {
                        // TODO save required data in preferences
                        try {
                            val jsonStr = body.string()
                            val `object` = JSONObject(jsonStr)
                            getListener()!!.handleSubmiResponse(`object`)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            getListener()!!.handleErrorResponse()
                        }
                    }
                }
            }

            override fun onFailure(
                code: Int,
                failureResponse: FailureResponse?
            ) {
                if (getListener() != null && failureResponse != null) {
                    getListener()!!.onErrorOccurred(failureResponse)
                }
            }

            override fun onError(t: Throwable?) {
                if (getListener() != null) {
                    getListener()!!.onErrorOccurred(null)
                }
            }
        })
    }

}