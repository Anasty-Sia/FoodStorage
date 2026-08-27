package com.example.foodstorage.presentation.vm

sealed class ProductEvent {
    object ProductSaved : ProductEvent()
    // object ShowError : ProductEvent()
    //  object ProductDelete : ProductEvent()

}