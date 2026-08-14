package com.example.foodstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.foodstorage.data.database.DatabaseProvider
import com.example.foodstorage.data.mapper.ProductMapper
import com.example.foodstorage.data.repository.ProductRepositoryImpl
import com.example.foodstorage.domain.usecase.AddProductUseCase
import com.example.foodstorage.domain.usecase.CountExpiredProductsUseCase
import com.example.foodstorage.domain.usecase.DeleteProductUseCase
import com.example.foodstorage.domain.usecase.GetAllProductsUseCase
import com.example.foodstorage.presentation.navigation.NavHostGraph
import com.example.foodstorage.presentation.vm.ProductViewModel
import com.example.foodstorage.ui.theme.FoodStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = DatabaseProvider.createDatabase(context)

        val dao = database.productDao()
        val repositoryImpl = ProductRepositoryImpl(
            dao = dao,
            mapper = ProductMapper()
        )

        val viewModel = ProductViewModel(
            GetAllProductsUseCase(repositoryImpl),
            AddProductUseCase(repositoryImpl),
            DeleteProductUseCase(repositoryImpl),
            CountExpiredProductsUseCase()
        )
        setContent {
            FoodStorageTheme {
                NavHostGraph(viewModel = viewModel)
            }

        }
    }
}

