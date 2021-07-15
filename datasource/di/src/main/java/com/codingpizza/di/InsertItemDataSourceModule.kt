package com.codingpizza.di

import com.codingpizza.cache.StoreItemDataSource
import com.codingpizza.euterpe.open.InsertItemDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class StoredItemDataSources

@InstallIn(SingletonComponent::class)
@Module
abstract class InsertItemDataSourceModule {

    @StoredItemDataSources
    @Singleton
    @Binds
    abstract fun bindStoreDataSource(implementation: StoreItemDataSource) : InsertItemDataSource

}
