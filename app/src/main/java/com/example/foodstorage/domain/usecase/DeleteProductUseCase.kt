package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val repository: ProductRepository
) {
    fun deleteProduct(id: Int){
        repository.deleteProduct(id)
    }
}