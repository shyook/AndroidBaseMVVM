package com.example.b23_hpt.core.extension

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.b23_hpt.BuildConfig

fun Activity.logd(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


