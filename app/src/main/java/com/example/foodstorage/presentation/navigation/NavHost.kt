package com.example.foodstorage.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodstorage.presentation.screen.AddProductScreen
import com.example.foodstorage.presentation.screen.ProductScreen
import com.example.foodstorage.presentation.vm.ProductViewModel

@Composable
fun NavHostGraph(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(
    navController = navController,
    startDestination = Routes.PRODUCT
    ) {
        composable(Routes.PRODUCT) {
            ProductScreen(viewModel = viewModel,
                onAdd = {navController.navigate(Routes.ADD_PRODUCTS)})
        }

        composable(Routes.ADD_PRODUCTS) {
            AddProductScreen (
                onBack = {navController.popBackStack()}
            )
        }
    }
}