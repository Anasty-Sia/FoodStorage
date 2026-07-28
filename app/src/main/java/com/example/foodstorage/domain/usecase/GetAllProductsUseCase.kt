package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository

class GetAllProductsUseCase(
    private val repository: ProductRepository
) {
    fun getAllProducts(): List<Product>{
        return repository.getAllProducts()
    }
}