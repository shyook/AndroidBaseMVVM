package com.example.b23_hpt.ui.main.repository

import com.example.b23_hpt.core.model.req.Credential
import com.example.b23_hpt.core.services.RetrofitService

class LocaleDataRepository(private val instance: RetrofitService) {
    suspend fun getLocalData() = instance.locales()

    suspend fun doLogin(credential: Credential?) = instance.login(credential)
}