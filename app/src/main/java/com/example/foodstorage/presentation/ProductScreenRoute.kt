package com.example.foodstorage.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.foodstorage.presentation.screen.ProductScreen
import com.example.foodstorage.presentation.vm.ProductViewModel

@Composable
fun ProductScreenRoute(viewModel: ProductViewModel) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts(showLoading = true)
    }
    ProductScreen(state)

}