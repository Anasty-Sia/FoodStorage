package com.example.foodstorage.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodstorage.presentation.screen.AddProductScreen
import com.example.foodstorage.presentation.screen.ProductScreen
import com.example.foodstorage.presentation.vm.ProductEvent
import com.example.foodstorage.presentation.vm.ProductViewModel

@Composable
fun NavHostGraph() {
    val viewModel = hiltViewModel<ProductViewModel>()

    val navController = rememberNavController()
    var selectedLocation by remember { mutableStateOf("") }
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.PRODUCT
    ) {
        composable(Routes.PRODUCT) {
            ProductScreen(
                viewModel = viewModel,
                onAdd = {
                    navController.navigate(
                        "${Routes.ADD_PRODUCTS}/0"
                    )
                },
                onUpdate = { productId ->
                    navController.navigate(
                        "${Routes.ADD_PRODUCTS}/$productId"
                    )
                }
            )

        }


        composable("${Routes.ADD_PRODUCTS}/{productId}") { backStackEntry ->


            val productId =
                backStackEntry.arguments
                    ?.getString("productId")
                    ?.toIntOrNull() ?: 0

            val isEditMode = productId != 0


            LaunchedEffect(Unit) {
                viewModel.sharedFlow.collect { event ->
                    when (event) {
                        ProductEvent.ProductSaved -> {
                            Toast.makeText(context, "Продукт сохранен", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        }

                    }

                }
            }
            AddProductScreen(
                productId = productId,
                isEditMode = isEditMode,
                onSave = { product -> viewModel.addProduct(product) },
                onUpdate = { product -> viewModel.updateProduct(product) },
                onBack = { navController.popBackStack() },
                selectedLocation = selectedLocation,
                onLocationChange = { location -> selectedLocation = location },
                viewModel = viewModel
            )
        }

    }
}