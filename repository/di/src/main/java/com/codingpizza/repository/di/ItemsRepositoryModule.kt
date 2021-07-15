package com.codingpizza.repository.di

import com.codingpizza.repository.implementation.ItemsRepositoryImpl
import com.codingpizza.repository.open.ItemsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class ItemRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
abstract class ItemsRepositoryModule {

    @ItemRepositoryImpl
    @Singleton
    @Binds
    abstract fun bindItemRepositoryImpl(implementation: ItemsRepositoryImpl) : ItemsRepository

}
