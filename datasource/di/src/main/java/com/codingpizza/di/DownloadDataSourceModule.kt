package com.codingpizza.di

import com.codingpizza.euterpe.open.DownloadsDataSource
import com.codingpizza.remote.DownloadDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DownloadDataSources

@InstallIn(SingletonComponent::class)
@Module
abstract class DownloadDataSourceModule {

    @DownloadDataSources
    @Singleton
    @Binds
    abstract fun bindDownloadDataSource(implementation: DownloadDataSourceImpl) : DownloadsDataSource

}
