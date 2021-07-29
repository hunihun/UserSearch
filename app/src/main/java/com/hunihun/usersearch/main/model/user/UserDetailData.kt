package com.hunihun.usersearch.main.model.user

data class UserDetailData(
    val bio: String = "",
    val login: String = "",
    val name: String = "",
    val profilePath: String = "",
    val repoName: String = "",
    val starCount: Int = 0,
    val pushedAt: String = "",
    val repoUrl: String = ""
)
