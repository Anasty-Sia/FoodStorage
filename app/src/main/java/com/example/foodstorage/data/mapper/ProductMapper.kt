package com.example.foodstorage.data.mapper

import com.example.foodstorage.data.database.ProductEntity
import com.example.foodstorage.domain.Product
import javax.inject.Inject


class ProductMapper @Inject constructor(){

    fun toEntity(product: Product): ProductEntity{
        return ProductEntity(
            id = product.id,
            name = product.name,
            quantity = product.quantity,
            storagePlace = product.storagePlace,
            shelfLife = product.shelfLife
        )

    }

    fun toDomain(entity: ProductEntity): Product{
        return Product(
            id = entity.id,
            name = entity.name,
            quantity = entity.quantity,
            storagePlace = entity.storagePlace,
            shelfLife = entity.shelfLife
        )
    }
}