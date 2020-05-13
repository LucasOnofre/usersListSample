package com.onoffrice.userslistsample.data.network.service.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.onoffrice.userslistsample.utils.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response


class ConnectivityInterceptorImpl(var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasNetworkConnection()) {
            throw NoConnectivityException("Sem conexão com a internet")
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun hasNetworkConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}