package com.example.b23_hpt.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import com.example.b23_hpt.AppApplication
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NetworkUtils {
    companion object {
        /**
         * Most websites serve cookies in the blessed format.
         * Eagerly create the parser to ensure such cookies are on the fast path.
         */
        private val HTTP_DATE_FORMAT: ThreadLocal<DateFormat?> =
            object : ThreadLocal<DateFormat?>() {
                override fun initialValue(): DateFormat {
                    // RFC 1123 (example: Mon, 15 Aug 2005 15:52:01 +0000)
                    val rfc1123: DateFormat =
                        SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)
                    rfc1123.timeZone = TimeZone.getTimeZone("GMT")
                    return rfc1123
                }
            }

        /**
         * Wi-Fi MAC Address 반환
         */
        fun macAddress(): String {
            val ctx: Context = AppApplication.INSTANCE.applicationContext   // Application에서 Context 획득
            val wifiManager = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = wifiManager.connectionInfo

            return connectionInfo.macAddress
        }

        fun formatHttpDate(date: Date?): String {
            return NetworkUtils.HTTP_DATE_FORMAT.get()?.format(date) ?: ""
        }

        fun isWifiEnabled(): Boolean {
            val wifi = AppApplication.INSTANCE.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wifi.isWifiEnabled
        }

        fun isConnectedWifi() : Boolean {
            val cm =
                AppApplication.INSTANCE.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        }
    }
}