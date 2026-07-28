package com.example.foodstorage.domain

import java.time.LocalDate

data class Product(
    val id: Int,
    var name: String,
    var quantity: Double,
    val storagePlace: String,
    val shelfLife: LocalDate,
) {
    fun isSameProduct(otherProduct: Product): Boolean {

        return name == otherProduct.name
                && storagePlace == otherProduct.storagePlace &&
                shelfLife == otherProduct.shelfLife
    }

    fun addQuantity(otherQuantity: Double){
        quantity += otherQuantity
    }
}