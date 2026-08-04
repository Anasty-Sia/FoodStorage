package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository
) {

    fun addProduct(product: Product): RepositoryResult<Unit> {
        val repositoryResult = repository.getAllProducts()
        when (repositoryResult) {
            is RepositoryResult.Success -> {

                for (currentProduct in repositoryResult.result) {
                    if (currentProduct.isSameProduct(product)) {
                        currentProduct.addQuantity(product.quantity)
                        return repository.updateProduct(currentProduct)
                    }
                }
                return repository.saveProduct(product)

            }

            is RepositoryResult.Error -> {
                return repositoryResult
            }
        }

    }
}
