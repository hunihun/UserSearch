package com.hunihun.usersearch.main.repository

import com.hunihun.usersearch.main.model.ResponseGitHubUserData
import io.reactivex.Single

interface UserSearchRepository {
    fun searchUser(query: String?, page: Int): Single<ResponseGitHubUserData>
}