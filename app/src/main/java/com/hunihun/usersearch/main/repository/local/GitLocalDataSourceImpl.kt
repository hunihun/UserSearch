package com.hunihun.usersearch.main.repository.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

class GitLocalDataSourceImpl @Inject constructor(): GitLocalDataSource {
}

@Module
@InstallIn(SingletonComponent::class)
abstract class GitLocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindGitLocalDataSource(repositoryImpl: GitLocalDataSourceImpl): GitLocalDataSource
}