package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    fun getAllProducts(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}