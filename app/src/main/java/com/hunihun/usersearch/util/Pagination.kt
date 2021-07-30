package com.hunihun.usersearch.util

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Pagination @Inject constructor() {
    var pageNo = 1
    var isMorePage = true
    var loadingData = false

    @Singleton
    fun initialize() {
        pageNo = 1
        isMorePage = true
    }

    @Singleton
    fun addPageNo() {
        pageNo++
    }
}