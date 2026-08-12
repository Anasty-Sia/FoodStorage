package com.example.foodstorage.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.foodstorage.domain.Product
import com.example.foodstorage.presentation.vm.ProductScreenState
import com.example.foodstorage.presentation.vm.ProductViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    onAdd: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val currentState = state


    LaunchedEffect(Unit) {
        viewModel.loadProducts(showLoading = true)
    }

    val showFab = when (currentState) {
        ProductScreenState.Empty -> {
            true
        }

        is ProductScreenState.Products -> {
            true
        }

        else -> {
            false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Food Storage",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.titleLarge
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        },

        floatingActionButton = {

            if (showFab) {

                FloatingActionButton(
                    onClick = onAdd,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Добавить"
                    )


                }
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {

            when (currentState) {
                ProductScreenState.Loading -> LoadingState()
                ProductScreenState.Empty -> EmptyState()
                is ProductScreenState.Products -> {
                    SearchBarBlock()
                    FiltersGroup()
                    Counter(currentState.totalProducts, currentState.expiredProducts)
                    ProductsList(currentState.products)

                }

                is ProductScreenState.Error -> {
                    ErrorScreen(
                        currentState.message,
                        onRetry = { viewModel.loadProducts(true) })
                }
            }


        }
    }
}

@Composable
fun ProductsList(products: List<Product>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp) // отступ между элементами
    ) {
        items(products) { product ->
            ProductCard(product)
        }
    }
}


@Composable
fun SearchBarBlock() {
    var search by remember { mutableStateOf("") }

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        label = { Text("Поиск") },
        placeholder = { Text("Поиск продуктов") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        singleLine = true,
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (search.isNotEmpty()) {
                IconButton(onClick = {
                    search = ""
                }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
    )
}


@Composable
fun FiltersGroup() {
    var selectedFilter by remember { mutableStateOf("Все") }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding()
    ) {
        listOf("Все", "Холодильник", "Полка").forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = {
                    selectedFilter = filter
                },
                label = { Text(filter) },
                leadingIcon =
                    {
                        when (filter) {
                            "Все" -> {
                                Icon(
                                    imageVector = Icons.Filled.Widgets,
                                    contentDescription = "Выбрано",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }

                            "Холодильник" -> {
                                Icon(
                                    imageVector = Icons.Filled.Kitchen,
                                    contentDescription = "Выбрано",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }

                            "Полка" -> {
                                Icon(
                                    imageVector = Icons.Filled.Inventory,
                                    contentDescription = "Выбрано",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        }
                    },
            )
        }
    }
}

@Composable
fun ProductCard(product: Product) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${product.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(
                    text = "${product.storagePlace}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = " До: ${product.shelfLife}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

        }

    }

}

@Composable
fun Counter(
    totalProducts: Int,
    expiredProducts: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Всего продуктов: $totalProducts"
        )

        Text(
            text = "Просрочено: $expiredProducts"
        )
    }

}





