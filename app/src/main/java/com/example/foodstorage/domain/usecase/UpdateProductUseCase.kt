package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun updateProduct(product: Product): RepositoryResult<Unit>{
        return repository.updateProduct(product)
    }
}


