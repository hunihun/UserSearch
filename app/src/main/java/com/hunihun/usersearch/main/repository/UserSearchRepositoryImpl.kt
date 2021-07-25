package com.hunihun.usersearch.main.repository

import com.hunihun.usersearch.main.model.ResponseGitHubUserData
import com.hunihun.usersearch.main.repository.local.GitLocalDataSource
import com.hunihun.usersearch.main.repository.remote.GitRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

class UserSearchRepositoryImpl @Inject constructor(
    private val gitRemoteDataSource: GitRemoteDataSource,
    private val gitLocalDataSource: GitLocalDataSource
): UserSearchRepository {

    override fun searchUser(query: String?, page: Int): Single<ResponseGitHubUserData> {
        return gitRemoteDataSource.searchUser(query, page)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserSearchRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserSearchRepository(repositoryImpl: UserSearchRepositoryImpl): UserSearchRepository
}