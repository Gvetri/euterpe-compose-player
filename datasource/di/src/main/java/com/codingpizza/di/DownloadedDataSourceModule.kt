package com.codingpizza.di

import com.codingpizza.cache.DownloadedItemDataSource
import com.codingpizza.euterpe.open.DownloadedDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DownloadedDataSources

@InstallIn(SingletonComponent::class)
@Module
abstract class DownloadedDataSourceModule {

    @DownloadedDataSources
    @Singleton
    @Binds
    abstract fun bindDownloadDataSource(implementation: DownloadedItemDataSource) : DownloadedDataSource

}
