package com.example.foodstorage.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    onAdd: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
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

        is ProductScreenState.EmptyFilter -> {
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

            if (currentState != ProductScreenState.Empty || currentState != ProductScreenState.Loading) {
                SearchBarBlock()
                FiltersGroup(
                    selectedFilter = selectedFilter,
                    onSelectionChange = { filter -> viewModel.updateFilter(filter) }
                )
            }

            when (currentState) {
                ProductScreenState.Loading -> LoadingState()
                ProductScreenState.Empty -> EmptyState()
                ProductScreenState.EmptyFilter -> EmptyFilterState()


                is ProductScreenState.Products -> {
                    Counter(currentState.totalProducts, currentState.expiredProducts)
                    ProductsList(
                        currentState.products,
                        onDelete = { id ->
                            viewModel.deleteProduct(id)
                        })
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
fun ProductsList(products: List<Product>, onDelete: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = products, key = { product -> product.id }) { product ->
            ProductCard(product, onDelete)
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
fun FiltersGroup(
    onSelectionChange: (String) -> Unit,
    selectedFilter: String
) {

    val listFilter = listOf("Все", "Холодильник", "Полка")


    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding()
    ) {
        listFilter.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = {
                    onSelectionChange(filter)
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
fun ProductCard(
    product: Product,
    onDelete: (Int) -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState(
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )

    SwipeToDismissBox(
        state = swipeState,
        onDismiss = { direction ->
            if (direction == SwipeToDismissBoxValue.EndToStart) {
                onDelete(product.id)
            }
        },
        backgroundContent = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.primary
                    )


                }
            }
        }
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
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
                        text = "${product.storagePlace}",
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
                        text = "${product.quantity}",
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

///
@Composable
private fun ActionButton(
    icon: Int,
    description: String,
    onClick: () -> Unit,
    isSwipeFullLeft: Boolean = false
) {
    var colorBackground = MaterialTheme.colorScheme.onSecondary
    var colorTInt = MaterialTheme.colorScheme.secondary

    if (isSwipeFullLeft) {
        colorBackground = MaterialTheme.colorScheme.secondary
        colorTInt = MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = colorBackground,
                shape = CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            modifier = Modifier.size(24.dp),
            tint = colorTInt
        )
    }
}




