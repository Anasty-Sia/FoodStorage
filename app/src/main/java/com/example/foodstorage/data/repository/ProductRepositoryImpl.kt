package com.example.foodstorage.data.repository

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {

    private val products = mutableListOf<Product>()

    override fun getAllProducts(): List<Product> {
        return products
    }

    override fun saveProduct(product: Product) {
        products.add(product)
    }

    override fun deleteProduct(id: Int) {
        val product = products.find {
            it.id == id
        }
        products.remove(product)
    }

    override fun updateProduct(product: Product) {
        //пока пусто
    }
}
