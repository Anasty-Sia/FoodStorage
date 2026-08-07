package com.example.foodstorage.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodstorage.presentation.vm.ProductScreenState

@Composable
fun ProductScreen(productScreenState: ProductScreenState) {

    when (productScreenState) {
        ProductScreenState.Loading -> LoadingState()
        ProductScreenState.Empty -> EmptyState()
        is ProductScreenState.Products -> ProductsScreen(
            productScreenState.products,
            productScreenState.totalProducts,
            productScreenState.expiredProducts)
        is ProductScreenState.Error -> ErrorScreen(
            productScreenState.message)
    }

}

@Composable
fun ErrorScreen(message: String) {
    TODO("Not yet implemented")
}

@Composable
fun EmptyState() {
    TODO("Not yet implemented")
}

@Preview
@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(44.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

