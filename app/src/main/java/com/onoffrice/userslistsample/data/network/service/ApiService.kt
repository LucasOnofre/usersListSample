package com.onoffrice.userslistsample.data.network.service

import android.content.Context
import com.onoffrice.userslistsample.BuildConfig
import com.onoffrice.userslistsample.data.network.service.interceptor.ConnectivityInterceptorImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ApiService {

    fun <S> createService(context: Context,url: String, serviceClass: Class<S>): S {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()))

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))

        httpClient.addInterceptor(ConnectivityInterceptorImpl(context))

        retrofit.client(httpClient.build())
        return retrofit.build().create(serviceClass)
    }
}