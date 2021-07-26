package com.hunihun.usersearch.retrofit

import com.hunihun.usersearch.main.model.repo.ResponseGitHubRepoData
import com.hunihun.usersearch.main.model.user.ResponseGitHubUserListData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitImpl {

    @GET("search/users")
    fun searchUser(@Query("q") q: String?, @Query("page") page: Int): Single<ResponseGitHubUserListData>

    @GET("users/{userId}/repos")
    fun searchRepo(@Path("userId") userId: String?, @Query("page") page: Int): Single<ResponseGitHubRepoData>
}