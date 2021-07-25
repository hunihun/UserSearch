package com.hunihun.usersearch.main.model

data class ResponseGitHubUserData(
    val incomplete_results: Boolean,
    val userLists: List<UserList>,
    val total_count: Int
)