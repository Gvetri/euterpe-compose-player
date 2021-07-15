package com.codingpizza.di

import com.codingpizza.cache.RetrieveCachedItemsDataSource
import com.codingpizza.euterpe.open.RetrieveItemsDataSource
import com.codingpizza.remote.RetrieveRemoteItemsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class RetrieveLocalDataSource

@Qualifier
annotation class RetrieveRemoteDataSource

@InstallIn(SingletonComponent::class)
@Module
abstract class RetrieveItemsDataSourceModule {

    @RetrieveLocalDataSource
    @Singleton
    @Binds
    abstract fun bindRetrieveLocalItemDataSource(implementation: RetrieveCachedItemsDataSource): RetrieveItemsDataSource

    @RetrieveRemoteDataSource
    @Singleton
    @Binds
    abstract fun bindRetrieveRemoteItemDataSource(implementation: RetrieveRemoteItemsDataSource): RetrieveItemsDataSource

}
