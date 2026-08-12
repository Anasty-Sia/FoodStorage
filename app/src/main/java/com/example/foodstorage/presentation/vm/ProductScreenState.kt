package com.example.foodstorage.presentation.vm

import com.example.foodstorage.domain.Product

sealed class ProductScreenState() {
    object Loading : ProductScreenState()
    object Empty : ProductScreenState()
    data class Products(
        val products: List<Product>,
        val totalProducts: Int,
        val expiredProducts: Int
    ): ProductScreenState()
    data class Error(
        val message: String
    ): ProductScreenState()

}