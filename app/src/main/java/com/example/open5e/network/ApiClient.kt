package com.example.open5e.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    public const val DEFAULT_BASE_URL = "https://api.open5e.com/"

    @Volatile
    var retrofit: Retrofit = createRetrofit(DEFAULT_BASE_URL)
        private set

    fun setBaseUrl(baseUrl: String) {
        retrofit = createRetrofit(baseUrl)
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
