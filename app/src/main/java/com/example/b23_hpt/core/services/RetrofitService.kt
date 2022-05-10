package com.example.b23_hpt.core.services

import com.example.b23_hpt.core.model.req.Credential
import com.example.b23_hpt.core.model.res.cloud.Locale
import com.example.b23_hpt.core.model.res.cloud.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
    @POST("/V2/Login/")
    fun login(@Body credential: Credential?): Call<Login?>

    @GET("/V3/Locales/")
    suspend fun locales(): List<Locale>?
}