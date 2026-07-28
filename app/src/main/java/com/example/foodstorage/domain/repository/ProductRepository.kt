package com.example.foodstorage.domain.repository

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult

interface ProductRepository {
    fun getAllProducts(): RepositoryResult<List<Product>>
    fun saveProduct(product: Product):RepositoryResult<Unit>  // сохранить продукт
    fun updateProduct(product: Product): RepositoryResult<Unit> // обновить существующий
    fun deleteProduct(id:Int):RepositoryResult<Unit> // удалить продукт
}