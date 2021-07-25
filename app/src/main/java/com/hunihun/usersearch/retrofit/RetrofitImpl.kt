package com.hunihun.usersearch.retrofit

import com.hunihun.usersearch.main.model.ResponseGitHubUserData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitImpl {

    @GET("search/users")
    fun searchUser(@Query("q") q: String?, @Query("page") page: Int): Single<ResponseGitHubUserData>
}