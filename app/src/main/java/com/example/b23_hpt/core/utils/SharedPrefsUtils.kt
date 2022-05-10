package com.example.b23_hpt.core.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.b23_hpt.AppApplication


class SharedPrefsUtils {
    companion object {
        private const val TAG = "SharedPrefsUtils"

        // shared pref key
        const val KEY_CLOUD_HOST_REST = "cloud.host.rest"
        const val KEY_CLOUD_APP_TOKEN = "cloud_app_token"
        const val KEY_CLOUD_EMAIL = "cloud_email"
        const val KEY_CLOUD_PREV_LANG = "cloud_lang"

        fun set(key: String, value: Any){
            val editor = sharedPreferences.edit()
            with(editor) {
                when (value) {
                    is Int -> {
                        android.util.Log.d(com.example.b23_hpt.core.utils.SharedPrefsUtils.TAG, "Input Key is $key, Int Value is $value ")
                        putInt(key, value)
                    }

                    is String -> {
                        android.util.Log.d(com.example.b23_hpt.core.utils.SharedPrefsUtils.TAG, "Input Key is $key, String Value is $value ")
                        putString(key, value)
                    }

                    is Boolean -> {
                        android.util.Log.d(com.example.b23_hpt.core.utils.SharedPrefsUtils.TAG, "Input Key is $key, Boolean Value is $value ")
                        putBoolean(key, value)
                    }
                    else -> throw IllegalArgumentException("잘못된 인자입니다.")
                }
                apply()
            }
        }

        fun getString(key: String) = sharedPreferences.getString(key, "")

        fun getInt(key: String) = sharedPreferences.getInt(key, 0)

        fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)

        fun remove(key: String) {
            with(sharedPreferences.edit()) {
                remove(key)
                commit()
            }
        }

        private val sharedPreferences: SharedPreferences by lazy {
            val masterKeyAlias = MasterKey
                .Builder(AppApplication.INSTANCE.applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                AppApplication.INSTANCE.applicationContext,
                AppApplication.INSTANCE.applicationContext.packageName,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM )
        }
    }

}