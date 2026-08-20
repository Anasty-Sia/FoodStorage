package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsOnceUseCase  @Inject constructor(
    private val repository: ProductRepository
){

    suspend fun getProductsOne(): List<Product>{
        return repository.getProductsOnce()
    }
}