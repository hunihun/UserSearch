package com.hunihun.usersearch.util

class Pagination {
    var pageNo = 1
    var isMorePage = true
    var loadingData = false

    fun initialize() {
        pageNo = 1
        isMorePage = true
    }

    fun addPageNo() {
        pageNo++
    }
}