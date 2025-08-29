package com.infinion.infinion.utils

object UtilityParam {
    init {
        System.loadLibrary("api-keys")
    }

    private external fun getApiKey(): String
    private external fun getBaseUrl(): String


    val API_KEY = getApiKey()
    val BASE_URL = getBaseUrl()
}
