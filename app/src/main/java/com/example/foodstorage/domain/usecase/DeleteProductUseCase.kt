package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val repository: ProductRepository
) {
    fun deleteProduct(id: Int): RepositoryResult<Unit>{
        return repository.deleteProduct(id)
    }
}