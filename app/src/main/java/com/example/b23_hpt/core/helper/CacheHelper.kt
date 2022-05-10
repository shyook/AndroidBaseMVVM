package com.example.b23_hpt.core.helper

import com.example.b23_hpt.core.model.res.group.CloudConnectionInfo
import com.example.b23_hpt.ui.main.viewmodel.MieleLocale

class CacheHelper private constructor() {
    private var cloudConnectionInfo: CloudConnectionInfo? = null
    private var mieleLocale: MieleLocale? = null

    companion object {
        @Volatile private var instance: CacheHelper? = null

        @JvmStatic fun getInstance(): CacheHelper =
            instance ?: synchronized(this) {
                instance ?: CacheHelper().also {
                    instance = it
                }
            }
    }

    // 클라우드 정보 저장
    fun getCloudConnectionInfo() : CloudConnectionInfo? = cloudConnectionInfo
    fun setCloudConnectionInfo(cloudConnectionInfo: CloudConnectionInfo?) {
        this.cloudConnectionInfo = cloudConnectionInfo
    }

    // locale 정보 저장
    fun getMieleLocale() : MieleLocale? = mieleLocale
    fun setMieleLocale(mieleLocale: MieleLocale?) {
        this.mieleLocale = mieleLocale
    }
}