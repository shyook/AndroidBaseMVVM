package com.example.b23_hpt.core.model.res.cloud

import com.google.gson.annotations.SerializedName

data class Locale (
    @SerializedName("active")
    val isActive: Boolean,
    val countries: List<Country>?,
    val contact: Contact?,
    val features: List<Feature>?,
    val mcsConfig: MCSConfig?
    ) {

    class Contact {
        @SerializedName("languages")
        var languages: List<ContactLangauge>? = null
    }

    class MCSConfig {
        @SerializedName("dnsNames")
        var dnsNames: List<MCSConfigItem>? = null
    }

    class MCSConfigItem {
        @SerializedName("host")
        var host: String? = null

        @SerializedName("type")
        var type: String? = null
    }

    class ContactLangauge {
        @SerializedName("iso")
        var iso: String? = null

        @SerializedName("isDefault")
        var isDefault = false
    }

    class Country {
        @SerializedName("iso")
        var iso: String? = null

        @SerializedName("name")
        var name: String? = null
    }

    class Feature {
        @SerializedName("available")
        var isAvailable = false

        @SerializedName("name")
        var name: String? = null

        @SerializedName("config")
        var config: List<FeatureConfig>? = null

        override fun toString(): String {
            return "Feature{" +
                    "available=" + isAvailable +
                    ", name='" + name + '\'' +
                    ", config=" + config +
                    '}'
        }
    }

    class FeatureConfig {
        @SerializedName("key")
        var key: String? = null

        @SerializedName("value")
        var value: String? = null

        override fun toString(): String {
            return "FeatureConfig{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}'
        }
    }
}