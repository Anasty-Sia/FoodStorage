package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository
) {

    fun addProduct(product: Product) {
        val allProducts = repository.getAllProducts()
        var isProductFound = false
        for (currentProduct in allProducts) {
            if (currentProduct.isSameProduct(product)) {
                isProductFound = true
                currentProduct.addQuantity(product.quantity)
                repository.updateProduct(currentProduct)
                break
            }
        }
        if (!isProductFound) {
            repository.saveProduct(product)
        }

    }
}
