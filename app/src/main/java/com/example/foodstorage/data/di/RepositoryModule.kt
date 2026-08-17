package com.example.foodstorage.data.di

import com.example.foodstorage.data.repository.ProductRepositoryImpl
import com.example.foodstorage.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        repository: ProductRepositoryImpl
    ): ProductRepository
}