package com.hunihun.usersearch.main.repository.remote

import com.hunihun.usersearch.main.model.repo.ResponseGitHubRepoData
import com.hunihun.usersearch.main.model.user.ResponseGitHubUserListData
import io.reactivex.Single

interface GitRemoteDataSource {
    fun searchUser(query: String?, page: Int): Single<ResponseGitHubUserListData>
    fun searchRepo(userId: String?, page: Int): Single<ResponseGitHubRepoData>
}