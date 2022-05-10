package com.example.b23_hpt

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log

class AppApplication: Application() {
    private val TAG: String = AppApplication.javaClass.simpleName

    private var appVersion = ""

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()

        try {
            appVersion =
                packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "error getting package info")
        }
    }

    fun getVersion() : String {
        return appVersion
    }

    companion object {
        lateinit var INSTANCE: AppApplication
    }
}