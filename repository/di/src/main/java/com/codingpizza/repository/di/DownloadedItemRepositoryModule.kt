package com.codingpizza.repository.di

import com.codingpizza.repository.implementation.DownloadedItemRepositoryImpl
import com.codingpizza.repository.open.DownloadedItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DownloadItemRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
abstract class DownloadedItemRepositoryModule {

    @DownloadItemRepositoryImpl
    @Singleton
    @Binds
    abstract fun bindDownloadItemRepositoryImpl(implementation: DownloadedItemRepositoryImpl): DownloadedItemRepository
}
