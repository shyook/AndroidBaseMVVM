package com.example.b23_hpt.ui.main.viewmodel

import java.io.Serializable

data class MieleLocale(
    val code: String,
    val endpointRest: String,
    val endpointWebsocket: String
) : Serializable {
    fun getCountryCode() : String {
        return code.split("[_-]")[1].lowercase()
    }
}
