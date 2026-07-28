package com.example.foodstorage.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.usecase.AddProductUseCase
import com.example.foodstorage.domain.usecase.DeleteProductUseCase
import com.example.foodstorage.domain.usecase.GetAllProductsUseCase
import com.example.foodstorage.presentation.screen.ProductScreenState
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

            val products = try {
                  withContext(Dispatchers.IO) {
                    getAllProductsUseCase.getAllProducts()
                }
            } catch (e: Exception) {
                _state.value = ProductScreenState.Error("Не удалось загрузить список продуктов. Попробуйте ещё раз.")
                return@launch
            }

            if (products.isEmpty()) {
                _state.value = ProductScreenState.Empty
                return@launch
            }
            _state.value = ProductScreenState.Products(products)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addProductUseCase.addProduct(product)
            }
            loadProducts(showLoading = false)
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteProductUseCase.deleteProduct(id)
            }
            loadProducts(showLoading = false)
        }
    }

}