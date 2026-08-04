package com.example.foodstorage.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.usecase.AddProductUseCase
import com.example.foodstorage.domain.usecase.DeleteProductUseCase
import com.example.foodstorage.domain.usecase.GetAllProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow<ProductScreenState>(ProductScreenState.Loading)
    val state: StateFlow<ProductScreenState> = _state


    fun loadProducts(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) {
                _state.value = ProductScreenState.Loading
            }

            val products = withContext(Dispatchers.IO) {
                getAllProductsUseCase.getAllProducts()
            }
            when (products) {
                is RepositoryResult.Success -> {
                    if (products.result.isEmpty()) {
                        _state.value = ProductScreenState.Empty
                        return@launch
                    }
                    _state.value = ProductScreenState.Products(products.result)

                }

                is RepositoryResult.Error -> {
                    _state.value =
                        ProductScreenState.Error("Не удалось загрузить список продуктов. Попробуйте ещё раз.")

                }
            }


        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
           val addResult =  withContext(Dispatchers.IO) {
                addProductUseCase.addProduct(product)
            }
            when(addResult){
                is RepositoryResult.Success -> {
                    loadProducts(showLoading = false)
                }
                is RepositoryResult.Error -> {
                    _state.value = ProductScreenState.Error(addResult.message)
                }
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            val deleteResult  = withContext(Dispatchers.IO) {
                deleteProductUseCase.deleteProduct(id)
            }
            when (deleteResult ) {
                is RepositoryResult.Success -> {
                    loadProducts(showLoading = false)

                }

                is RepositoryResult.Error -> {
                    _state.value = ProductScreenState.Error(deleteResult.message)

                }
            }
        }
    }

}