package com.hunihun.usersearch.main.model.user

data class ResponseGitHubUserListData(
        val incomplete_results: Boolean,
        val items: List<UserListData>,
        val total_count: Int
)