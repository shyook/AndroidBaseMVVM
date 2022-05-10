package com.example.b23_hpt.core.model.res.group

import android.text.TextUtils
import com.example.b23_hpt.core.utils.SharedPrefsUtils

object CloudConnectionInfo {
    var lang: String = ""
    var appToken: String = ""
    var hostRest: String = ""
    var email: String = ""
    var hostname: String = ""

    /**
     * sharedPrefs 영역에 있는 cloud 로그인 정보 로드
     */
    fun loadFromSharedPrefs() {
        hostRest = SharedPrefsUtils.getString(SharedPrefsUtils.KEY_CLOUD_HOST_REST) ?: ""
        appToken = SharedPrefsUtils.getString(SharedPrefsUtils.KEY_CLOUD_APP_TOKEN) ?: ""
        email = SharedPrefsUtils.getString(SharedPrefsUtils.KEY_CLOUD_EMAIL) ?: ""
        lang = SharedPrefsUtils.getString(SharedPrefsUtils.KEY_CLOUD_PREV_LANG) ?: "en-gb"
    }

    /**
     * 로그인 여부 반환 (appToken이 존재하면 로그인)
     * true is logged in
     */
    fun isLoggedIn(): Boolean {
        return !TextUtils.isEmpty(appToken)
    }
}