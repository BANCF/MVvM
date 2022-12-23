package com.example.mvvm.api

import com.example.mvvm.utils.Constants.API_KEY
import com.example.mvvm.utils.Constants.BASEURL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private lateinit var    retrofit: Retrofit

    private val requestInterceptor = Interceptor{chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("api_key",API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
           .build()

        return@Interceptor chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    fun getClient(): Retrofit{
        retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit

    }
}