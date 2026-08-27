package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun addProduct(product: Product): RepositoryResult<Unit> {
        val products = repository.getProductsOnce()
        for (currentProduct in products) {
            if (currentProduct.isSameProduct(product)) {
                currentProduct.addQuantity(product.quantity)
                return repository.updateProduct(currentProduct)
            }
        }
        return repository.saveProduct(product)
    }
}
