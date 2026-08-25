package com.example.foodstorage.domain.usecase

import com.example.foodstorage.domain.Product
import java.time.LocalDate
import javax.inject.Inject

class CountExpiredProductsUseCase @Inject constructor(){
    fun countExpiredProducts(products: List<Product>): Int {
        var count = 0
        val today = LocalDate.now()
        for (product  in products) {
            if (product.shelfLife < today) {
                count++
            }
        }
        return count
    }
}