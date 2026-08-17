package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun deleteProduct(id: Int): RepositoryResult<Unit>{
        return repository.deleteProduct(id)
    }
}