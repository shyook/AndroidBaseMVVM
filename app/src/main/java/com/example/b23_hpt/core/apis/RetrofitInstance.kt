package com.example.b23_hpt.core.apis

import com.example.b23_hpt.AppApplication
import com.example.b23_hpt.BuildConfig
import com.example.b23_hpt.core.consts.Constants
import com.example.b23_hpt.core.consts.Constants.Companion.HTTP
import com.example.b23_hpt.core.model.res.group.CloudConnectionInfo
import com.example.b23_hpt.core.services.RetrofitService
import com.example.b23_hpt.core.utils.NetworkUtils
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * 하나의 base url을 이용한 서버 통신 (밀레 서버에서 동적으로 url 설정하지 못하는 문제 발생)
 */
object RetrofitInstance {
    // base url 설정
    private var BASE_URL = HTTP + BuildConfig.HOST_REST

    // retrofit build
    private val retrofit by lazy {
        // Retrofit 빌더
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

//    private lateinit var retrofit: Retrofit
//    fun initRetrofit() {
//        retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(provideOkHttpClient(""))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(RetrofitService::class.java)
//    }
//
//    fun changeApiBaseUrl(newApiBaseUrl: String, acceptLang: String) {
//        BASE_URL = HTTP + newApiBaseUrl
//
//        retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(provideOkHttpClient(acceptLang))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(RetrofitService::class.java)
//    }

    private fun provideOkHttpClient(lang: String): OkHttpClient = OkHttpClient.Builder()
        .run {
            connectTimeout(15, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            readTimeout(Constants.CLOUD_MODE_READ_TIMEOUT, TimeUnit.SECONDS)
            followRedirects(false)
            followSslRedirects(false)

            var specs = ArrayList<ConnectionSpec>()
            specs.add(ConnectionSpec.CLEARTEXT)
            specs.add(ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledTlsVersions()
                .allEnabledCipherSuites()
                .build())
            connectionSpecs(specs)

            // log interceptor 설정
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            addInterceptor(ApplicationInterceptor(lang))
            addNetworkInterceptor(interceptor)

            build()
        }
//
//    /**
//     * 로그 출력을 위한 Interceptor
//     */
//    class LogInterceptor : Interceptor {
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain)
//                : Response = with(chain) {
//            val req = chain.request()
//
//            // Header Setting
//            val newRequest = request().newBuilder()
//                .addHeader("Accept-Language", lang)
//                .addHeader("Content-Type", Constants.MIELE_MIME_TYPE + "; charset=utf-8")
//                .header("Date", NetworkUtils.formatHttpDate(Date()))
//                .header("User-Agent", "APP-RX2-Android/v" + AppApplication.INSTANCE.getVersion())
//
//            // 앱 토큰 설정
//            if (appToken != null && !(req.method() == "DELETE" && req.url().encodedPath()
//                    .contains("/CustomerProfile"))
//            ) {
//                newRequest.addHeader("Authorization", "appToken $appToken")
//            }
//            proceed(newRequest.build())
//        }
//    }

    class ApplicationInterceptor(val acceptLang: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            var req = chain.request()
            val lang = if (acceptLang.isEmpty()) "de-de" else acceptLang

            val newRequest = request().newBuilder()
                .header("Accept-Language", lang)
                .header("Content-Type", Constants.MIELE_MIME_TYPE + "; charset=utf-8")
                .header("Date", NetworkUtils.formatHttpDate(Date()))
                .header("User-Agent", "APP-RX2-Android/v" + AppApplication.INSTANCE.getVersion())

            val path = req.url.encodedPath
            if (!path.startsWith("/V2")) {
                val host: String = CloudConnectionInfo.hostname
                if (host.isNotEmpty()) {
                    newRequest.header("Host", host)
                }
            }
            req = newRequest.build()
            proceed(req)
        }
    }
}