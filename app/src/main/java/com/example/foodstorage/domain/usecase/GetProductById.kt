package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductById @Inject constructor(
    private val repository: ProductRepository
) {

    suspend fun getProductById(id: Int): Product{

        return repository.getProductById(id)
    }
}