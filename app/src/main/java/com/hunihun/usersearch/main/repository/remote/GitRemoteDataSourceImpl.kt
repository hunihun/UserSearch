package com.hunihun.usersearch.main.repository.remote

import com.hunihun.usersearch.main.model.ResponseGitHubUserData
import com.hunihun.usersearch.retrofit.RetrofitImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

class GitRemoteDataSourceImpl @Inject constructor(
    private val retrofit: RetrofitImpl
): GitRemoteDataSource {
    override fun searchUser(query: String?, page: Int): Single<ResponseGitHubUserData> {
        return retrofit.searchUser(query, page)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class GitRemoteDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindGitRemoteDataSource(repositoryImpl: GitRemoteDataSourceImpl): GitRemoteDataSource
}