package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import java.time.LocalDate

class CountExpiredProductsUseCase{
    fun countExpiredProducts(products: List<Product>): Int {
        var count = 0
        val today = LocalDate.now()
        for (product  in products) {
            if (product .shelfLife < today) {
                count++
            }
        }
        return count
    }
}