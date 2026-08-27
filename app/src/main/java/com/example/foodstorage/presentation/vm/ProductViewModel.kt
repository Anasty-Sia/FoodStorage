package com.example.foodstorage.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.usecase.AddProductUseCase
import com.example.foodstorage.domain.usecase.CountExpiredProductsUseCase
import com.example.foodstorage.domain.usecase.DeleteProductUseCase
import com.example.foodstorage.domain.usecase.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val countExpiredProductsUseCase: CountExpiredProductsUseCase
) : ViewModel() {
    private var _state = MutableStateFlow<ProductScreenState>(ProductScreenState.Loading)
    val state: StateFlow<ProductScreenState> = _state

    private var _sharedFlow = MutableSharedFlow<ProductEvent>()
    val sharedFlow: SharedFlow<ProductEvent> = _sharedFlow

    private val _isSaving = MutableStateFlow(false)

    private var allProducts = emptyList<Product>()

    private val _selectedFilter = MutableStateFlow(STORAGE_PLACE_ALL)
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    fun loadProducts(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) {
                _state.value = ProductScreenState.Loading
            }

            try {
                getAllProductsUseCase.getAllProducts().collect { products ->
                    allProducts = products
                    applyFilters()
                }

            } catch (e: Exception) {
                _state.value =
                    ProductScreenState.Error("Не удалось загрузить список продуктов. Попробуйте ещё раз.")
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            if (_isSaving.value) {
                return@launch
            }
            _isSaving.value = true
            val addResult = withContext(Dispatchers.IO) {
                addProductUseCase.addProduct(product)
            }
            when (addResult) {
                is RepositoryResult.Success -> {
                    _isSaving.value = false
                    _sharedFlow.emit(ProductEvent.ProductSaved)
                }

                is RepositoryResult.Error -> {
                    _isSaving.value = false
                    _state.value = ProductScreenState.Error(addResult.message)
                }

            }

        }
    }

    fun deleteProduct(id: Int) {

        viewModelScope.launch {
            val deleteResult = withContext(Dispatchers.IO) {
                deleteProductUseCase.deleteProduct(id)
            }
            when (deleteResult) {
                is RepositoryResult.Success -> {
                }

                is RepositoryResult.Error -> {
                    _state.value = ProductScreenState.Error(deleteResult.message)

                }
            }
        }
    }


    fun searchText(text: String) {
        _searchQuery.value = text
        applyFilters()
    }


    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        applyFilters()
    }

    private fun applyFilters() {

        val filteredByPlace = when (_selectedFilter.value) {
            STORAGE_PLACE_ALL -> {
                allProducts
            }

            STORAGE_PLACE_FRIDGE -> {
                allProducts.filter { it.storagePlace == STORAGE_PLACE_FRIDGE }
            }

            STORAGE_PLACE_SHELF -> {
                allProducts.filter { it.storagePlace == STORAGE_PLACE_SHELF }
            }

            else -> allProducts
        }
        val searchQuery = _searchQuery.value
        val filteredBySearch = if (searchQuery.isEmpty()) {
            filteredByPlace
        } else {
            filteredByPlace.filter { product ->
                product.name.lowercase().contains(searchQuery.lowercase())
            }
        }

        when {
            allProducts.isEmpty() -> {
                _state.value = ProductScreenState.Empty
            }

            filteredBySearch.isEmpty() && searchQuery.isNotEmpty() -> {
                _state.value = ProductScreenState.EmptySearch
            }


            filteredByPlace.isEmpty() && _selectedFilter.value != STORAGE_PLACE_ALL -> {
                _state.value = ProductScreenState.EmptyFilter
            }

            filteredBySearch.isNotEmpty() -> {
                val expiredProducts =
                    countExpiredProductsUseCase.countExpiredProducts(filteredBySearch)
                _state.value = ProductScreenState.Products(
                    filteredBySearch,
                    filteredBySearch.size,
                    expiredProducts
                )
            }

            else -> {
                _state.value = ProductScreenState.EmptyFilter
            }

        }
    }

    companion object {
        const val STORAGE_PLACE_ALL = "Все"
        const val STORAGE_PLACE_FRIDGE = "Холодильник"
        const val STORAGE_PLACE_SHELF = "Полка"
    }

}