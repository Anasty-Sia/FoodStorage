package com.example.foodstorage.presentation.screen

import com.example.foodstorage.domain.Product

sealed class ProductScreenState {
    object Loading : ProductScreenState()
    object Empty : ProductScreenState()
    data class Products(val products: List<Product>): ProductScreenState()
    data class Error(val message: String): ProductScreenState()

}