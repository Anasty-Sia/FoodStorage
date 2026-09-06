package com.example.foodstorage.domain.repository

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductsOnce(): List<Product>
    suspend fun getProductById(id: Int): Product
    suspend fun saveProduct(product: Product):RepositoryResult<Unit>
    suspend fun updateProduct(product: Product): RepositoryResult<Unit>
    suspend fun deleteProduct(id:Int):RepositoryResult<Unit>
}