package com.example.foodstorage.domain.repository

import com.example.foodstorage.domain.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
    fun saveProduct(product: Product) // сохранить продукт
    fun updateProduct(product: Product) // обновить существующий
    fun deleteProduct(id:Int) // удалить продукт
}