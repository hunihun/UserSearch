package com.hunihun.usersearch.main.repository.remote

import com.hunihun.usersearch.main.model.ResponseGitHubUserData
import io.reactivex.Single

interface GitRemoteDataSource {
    fun searchUser(query: String?, page: Int): Single<ResponseGitHubUserData>
}