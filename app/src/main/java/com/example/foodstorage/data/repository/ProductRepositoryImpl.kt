package com.example.foodstorage.data.repository

import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository


class ProductRepositoryImpl : ProductRepository {

    private val products = mutableListOf<Product>()

    override fun getAllProducts(): RepositoryResult<List<Product>> {
        return RepositoryResult.Success(products)
    }

    override fun saveProduct(product: Product): RepositoryResult<Unit> {
        products.add(product)
        return RepositoryResult.Success(Unit)

    }

    override fun deleteProduct(id: Int): RepositoryResult<Unit> {

        val product = products.find {
            it.id == id
        }
        if(product == null){
            return RepositoryResult.Error("Продукт не найден")
        }
        products.remove(product)
        return RepositoryResult.Success(Unit)
    }

    override fun updateProduct(product: Product): RepositoryResult<Unit> {
       TODO()
    }
}
