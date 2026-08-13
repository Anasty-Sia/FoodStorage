package com.example.foodstorage.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodstorage.presentation.screen.AddProductScreen
import com.example.foodstorage.presentation.screen.ProductScreen
import com.example.foodstorage.presentation.vm.ProductViewModel

@Composable
fun NavHostGraph(viewModel: ProductViewModel) {
    val navController = rememberNavController()
    var selectedLocation by remember { mutableStateOf("") }

    NavHost(
    navController = navController,
    startDestination = Routes.PRODUCT
    ) {
        composable(Routes.PRODUCT) {
            ProductScreen(viewModel = viewModel,
                onAdd = {navController.navigate(Routes.ADD_PRODUCTS)})
        }

        composable(Routes.ADD_PRODUCTS) {
            AddProductScreen(
                onSave = { product -> viewModel.addProduct(product) },
                onBack = { navController.popBackStack() },
                selectedLocation = selectedLocation,
                onLocationChange = {location -> selectedLocation = location}
            )
        }
    }
}