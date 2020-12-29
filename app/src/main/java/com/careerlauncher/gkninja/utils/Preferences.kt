package com.careerlauncher.gkninja.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Preferences private constructor() {
    private val preferenceName = "com.mobiquel.claspiration"
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val KEY_SELECTED_QUESTION_D = "SELECTED_QUESTION_D"
    private val KEY_SHOW_GAME_RULE = "SHOW_GAME_RULE"
    private val KEY_IS_TOPIC_TEST_CONDUCTED = "IS_TOPIC_TEST_CONDUCTED"
    private val KEY_IS_WEB_LOGIN_DONE = "IS_WEB_LOGIN_DONE"
    private val KEY_IS_TODAY_GK_TEST_CONDUCTED = "IS_TODAY_GK_TEST_CONDUCTED"
    private val KEY_IS_YESTERDAY_GK_TEST_CONDUCTED = "IS_YESTERDAY_GK_TEST_CONDUCTED"
    private val KEY_YESTERDAY_GK_TEST_ID = "YESTERDAY_GK_TEST_ID"
    private val KEY_TODAY_GK_TEST_ID = "TODAY_GK_TEST_ID"
    private val KEY_TEST_LANGUAGE = "TEST_LANGUAGE"
    private val KEY_BASE_URL = "BASE_URL"
    private val KEY_VIDEO_TITLE = "VIDEO_TITLE"
    private val KEY_JSON_DATA = "JSON_DATA"
    private val KEY_OLD_VERSION_NUM = "OLD_VERSION_NUM"
    private val KEY_NEW_VERSION_NUM = "NEW_VERSION_NUM"
    private val KEY_UPDATE_DIALOG_STATS = "UPDATE_DIALOG_STATS"
    private val KEY_VIEW_TYPE = "VIEW_TYPE"
    private val KEY_WEB_VIEW = "WEB_VIEW"
    private val KEY_WEB_VIEW_HINDI = "WEB_VIEW_HINDI"
    private val KEY_DASH_BOARD_DATA = "DASH_BOARD_DATA"
    private val KEY_GAME_SOUND = "GAME_SOUND"
    private val KEY_GENDER = "GENDER"
    private val KEY_TERMS_CONDI = "TERMS_CONDI"
    var selectedQuestionId: String? = null
    var showGameRule: String? = null
    var dashboardData: String? = null
    var termsAndCondi: String? = null
    var gender: String? = null
    var gameSound: String? = null
    var webViewDataHindi: String? = null
    var webViewData: String? = null
    var viewType: String? = null
    var oldVersionNumber: String? = null
    var newVersionNumber: String? = null
    var updateDialogStats: String? = null
    var videoTitle: String? = null
    var jsonData: String? = null
    var baseUrl: String? = null
    var language: String? = null
    var isTopicTestConducted: String? = null
    var isWebLoginDone: String? = null
    var todayGkQsetId: String? = null
    var yesterdayGkQsetId: String? = null
    var isTodayGKestConducted: String? = null
    var isYesterdayGKTestConducted: String? = null
    fun loadPreferences(c: Context) {
        preferences = c.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE)
        selectedQuestionId = preferences?.getString(KEY_SELECTED_QUESTION_D, "0")
        showGameRule = preferences?.getString(KEY_SHOW_GAME_RULE, "1")
        isTopicTestConducted = preferences?.getString(KEY_IS_TOPIC_TEST_CONDUCTED, "0")
        gameSound = preferences?.getString(KEY_GAME_SOUND, "1")
        isTodayGKestConducted = preferences?.getString(KEY_IS_TODAY_GK_TEST_CONDUCTED, "0")
        isYesterdayGKTestConducted = preferences?.getString(KEY_IS_YESTERDAY_GK_TEST_CONDUCTED, "0")
        language = preferences?.getString(KEY_TEST_LANGUAGE, "English")
        todayGkQsetId = preferences?.getString(KEY_TODAY_GK_TEST_ID, "0")
        yesterdayGkQsetId = preferences?.getString(KEY_YESTERDAY_GK_TEST_ID, "0")
        isWebLoginDone = preferences?.getString(KEY_IS_WEB_LOGIN_DONE, "0")
        baseUrl = preferences?.getString(KEY_BASE_URL, "https://www.aspiration.link/endpoints/")
        videoTitle = preferences?.getString(KEY_VIDEO_TITLE, "")
        jsonData = preferences?.getString(KEY_JSON_DATA, "")
        dashboardData = preferences?.getString(KEY_DASH_BOARD_DATA, "")
        termsAndCondi = preferences?.getString(KEY_TERMS_CONDI, "")
        updateDialogStats = preferences?.getString(KEY_UPDATE_DIALOG_STATS, "0")
        newVersionNumber = preferences?.getString(KEY_NEW_VERSION_NUM, "")
        oldVersionNumber = preferences?.getString(KEY_OLD_VERSION_NUM, "")
        viewType = preferences?.getString(KEY_VIEW_TYPE, "LISTVIEW")
        webViewData = preferences?.getString(KEY_WEB_VIEW, "")
        webViewDataHindi = preferences?.getString(KEY_WEB_VIEW_HINDI, "")
        gender = preferences?.getString(KEY_GENDER, "")
    }

    fun savePreferences(c: Context) {
        preferences = c.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE)
        editor = preferences?.edit()
        editor?.putString(KEY_SELECTED_QUESTION_D, selectedQuestionId)
        editor?.putString(KEY_SHOW_GAME_RULE, showGameRule)
        editor?.putString(KEY_GAME_SOUND, gameSound)
        editor?.putString(KEY_IS_TOPIC_TEST_CONDUCTED, isTopicTestConducted)
        editor?.putString(KEY_IS_TODAY_GK_TEST_CONDUCTED, isTodayGKestConducted)
        editor?.putString(KEY_TEST_LANGUAGE, language)
        editor?.putString(KEY_IS_YESTERDAY_GK_TEST_CONDUCTED, isYesterdayGKTestConducted)
        editor?.putString(KEY_TODAY_GK_TEST_ID, todayGkQsetId)
        editor?.putString(KEY_YESTERDAY_GK_TEST_ID, yesterdayGkQsetId)
        editor?.putString(KEY_IS_WEB_LOGIN_DONE, isWebLoginDone)
        editor?.putString(KEY_BASE_URL, baseUrl)
        editor?.putString(KEY_JSON_DATA, jsonData)
        editor?.putString(KEY_VIDEO_TITLE, videoTitle)
        editor?.putString(KEY_DASH_BOARD_DATA, dashboardData)
        editor?.putString(KEY_GENDER, gender)
        editor?.putString(KEY_TERMS_CONDI, termsAndCondi)
        editor?.putString(KEY_OLD_VERSION_NUM, oldVersionNumber)
        editor?.putString(KEY_NEW_VERSION_NUM, newVersionNumber)
        editor?.putString(KEY_UPDATE_DIALOG_STATS, updateDialogStats)
        editor?.putString(KEY_VIEW_TYPE, viewType)
        editor?.putString(KEY_WEB_VIEW, webViewData)
        editor?.putString(KEY_WEB_VIEW_HINDI, webViewDataHindi)
        editor?.commit()
    }

    companion object {
        @get:Synchronized
        var instance: Preferences? = null
            get() {
                if (field == null) field =
                    Preferences()
                return field
            }
            private set
    }
}