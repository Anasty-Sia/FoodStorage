package com.example.foodstorage.presentation.vm

import android.util.Log
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

    private var isSaving = false

    private var allProducts = emptyList<Product>()

    private val _selectedFilter = MutableStateFlow(STORAGE_PLACE_ALL)
    val selectedFilter: StateFlow<String> = _selectedFilter


    fun loadProducts(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) {
                _state.value = ProductScreenState.Loading
            }


            try {
                getAllProductsUseCase.getAllProducts().collect { products ->
                    allProducts = products
                    Log.d("ALL","allProducts обновился: $allProducts")
                    if (products.isEmpty()) {

                        _state.value = ProductScreenState.Empty

                    } else {
                        updateFilter(selectedFilter.value)
                    }
                }

            } catch (e: Exception) {
                _state.value =
                    ProductScreenState.Error("Не удалось загрузить список продуктов. Попробуйте ещё раз.")
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            if (isSaving) {
                return@launch
            }
            isSaving = true
            val addResult = withContext(Dispatchers.IO) {
                addProductUseCase.addProduct(product)
            }
            when (addResult) {
                is RepositoryResult.Success -> {
                    isSaving = false
                    _sharedFlow.emit(ProductEvent.ProductSaved)
                }

                is RepositoryResult.Error -> {
                    isSaving = false
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

    fun updateFilter(filter: String){

        _selectedFilter.value = filter
        filterStoragePlace(filter)
    }


    fun filterStoragePlace(storagePlace: String) {

        when (storagePlace) {

            STORAGE_PLACE_ALL -> {
                productsList(allProducts)
            }

            STORAGE_PLACE_FRIDGE -> {
                val filterPlaceFridge =
                    allProducts.filter { it.storagePlace == STORAGE_PLACE_FRIDGE }
                productsList(filterPlaceFridge)


            }

            STORAGE_PLACE_SHELF -> {
                val filterPlaceShelf =
                    allProducts.filter { it.storagePlace == STORAGE_PLACE_SHELF }
                productsList(filterPlaceShelf)
            }
        }
    }


    fun productsList(list: List<Product>) {
        if (list.isEmpty()) {
            _state.value = ProductScreenState.EmptyFilter

        } else {
            val expiredProducts =
                countExpiredProductsUseCase.countExpiredProducts(list)
            _state.value = ProductScreenState.Products(
                list,
                list.size,
                expiredProducts
            )

        }

    }

    companion object {
        const val STORAGE_PLACE_ALL = "Все"
        const val STORAGE_PLACE_FRIDGE = "Холодильник"
        const val STORAGE_PLACE_SHELF = "Полка"
    }

}