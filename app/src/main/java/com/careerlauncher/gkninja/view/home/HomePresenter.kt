package com.careerlauncher.gkninja.view.home

import com.careerlauncher.gkninja.base.BasePresenter
import com.careerlauncher.gkninja.pojo.LogoutResponseModel
import org.json.JSONObject

class HomePresenter(view: HomeView?) : BasePresenter<HomeView?>(view),
    HomeModelListener {
    private var model: HomeModel? = null
    override fun setModel() {
        model = HomeModel(this)
    }

    override fun destroy() {
        if (model != null) model!!.detachListener()
        model = null
    }

    override fun initView() {

    }

    fun onLogout() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.logout()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }


    fun addfeedBack(data: List<String?>) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.submitFeedback(data)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }//  getView().showNoNetworkError();

    fun registerAppVersion(versionName: String, versionNo: String, deviceId: String) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.registerAppVersion(versionName, versionNo, deviceId)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getRankData() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getRankData()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }


    fun getTermsAndCondition() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getTermsAndCondition()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getAppVersion(versionName: String, versionNo: String, deviceId: String) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getAppVersion(versionName, versionNo, deviceId)
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getProfileData() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable == true) {
                getView()!!.showProgressBar()
                model!!.getProfileData()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }
    fun getProfileData_1() {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable == true) {
                getView()!!.showProgressBar()
                model!!.getProfileData_1()
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getGKQuizQuestion(testStatus: String, type: String, seqNo: String, qsetId: String) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getGKQuizQuestion(testStatus, type, seqNo, qsetId, "GK")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }


    fun getLegalQuestion(
        testStatus: String,
        type: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getGKQuizQuestion(testStatus, type, seqNo, qsetId, "LEGAL")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getVoabQuestion(
        testStatus: String,
        type: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getGKQuizQuestion(testStatus, type, seqNo, qsetId, "VOCAB")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun getFactsQuestion(
        testStatus: String,
        type: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model!!.getGKQuizQuestion(testStatus, type, seqNo, qsetId, "FACTS")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }


    fun submitGKQuizQuestion(
        success: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                   model?.submitGameQuestion(success, seqNo, qsetId,"GK")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun submitLegalQuizQuestion(
        success: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                  model?.submitGameQuestion(success, seqNo, qsetId,"LEGAL")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun submitFactsQuizQuestion(
        success: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                model?.submitGameQuestion(success, seqNo, qsetId,"FACTS")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun submitVocabQuestion(
        success: String,
        seqNo: String,
        qsetId: String
    ) {
        if (getView() != null) {
            if (getView()!!.isNetworkAvailable) {
                getView()!!.showProgressBar()
                    model?.submitGameQuestion(success, seqNo, qsetId,"VOCAB")
            } else {
                getView()!!.showNoNetworkError()
            }
        }
    }

    fun setUpBottomNavigationView() {
        if (getView() != null) {
            getView()!!.setUpBottomNavigationView()
        }
    }

    override fun handleLogoutResult(body: LogoutResponseModel?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (body != null) {
                if (body.logoutStatus == "success") getView()!!.showLoginSignUp() else getView()!!.showErrorMessage()
            }
        }
    }

    override fun handleBannerURLsResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
        }
    }

    override fun handleErrorResponse() {
        if (getView() != null) {
            getView()!!.hideProgressBar()
        }
    }

    override fun handleSubmiResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.setUpRankData(jsonObject)
            }
        }
    }

    override fun handleRegisterAppResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.registerAppSubmit(jsonObject)
            }
        }
    }

    override fun handleGetAppVersionResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.getAppVersion(jsonObject)
            }
        }
    }

    override fun handleRankResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.setUpRankData(jsonObject)
            }
        }
    }

    override fun handleQuizResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.setUpGameQuestion(jsonObject)
            }
        }
    }

    override fun handleTermsConditionResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.setUpData(jsonObject)
            }
        }
    }

    override fun handleProfileDataResponse(jsonObject: JSONObject?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            if (jsonObject != null) {
                getView()!!.setUpRankData(jsonObject)
            }
        }
    }
}