package com.hunihun.usersearch.main.model

data class ResponseGitHubUserData(
    val incomplete_results: Boolean,
    val items: List<UserListData>,
    val total_count: Int
)