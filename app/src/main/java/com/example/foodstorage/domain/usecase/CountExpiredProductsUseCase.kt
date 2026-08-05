package com.example.foodstorage.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository
import java.time.LocalDate

class CountExpiredProductsUseCase(
    private val repository: ProductRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun countExpiredProducts(expiredProducts: Int){
        val repositoryResult = repository.getAllProducts()
        when(repositoryResult){
            is RepositoryResult.Success ->{
                var count = 0
                for (currentProduct in repositoryResult.result) {
                    if (currentProduct.shelfLife < LocalDate.now()){
                        count++
                    }
                }

            }
            is RepositoryResult.Error -> return
        }


    }
}