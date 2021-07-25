package com.hunihun.usersearch.retrofit

import com.hunihun.usersearch.R
import com.hunihun.usersearch.util.App
import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", App.instance.getString(R.string.git_token))
            .build()

        return chain.proceed(newRequest)
    }
}