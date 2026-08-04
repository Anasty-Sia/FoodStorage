package com.example.foodstorage.presentation.screen

import androidx.compose.runtime.Composable
import com.example.foodstorage.presentation.vm.ProductScreenState

@Composable
fun ProductScreen(productScreenState: ProductScreenState) {

    when (productScreenState) {
        ProductScreenState.Loading -> LoadingState()
        ProductScreenState.Empty -> EmptyState()
        is ProductScreenState.Products -> ProductsScreen(productScreenState.products)
        is ProductScreenState.Error -> ErrorScreen(productScreenState.message)
    }

}

@Composable
fun EmptyState() {
    TODO("Not yet implemented")
}

@Composable
fun LoadingState() {
    TODO("Not yet implemented")
}

