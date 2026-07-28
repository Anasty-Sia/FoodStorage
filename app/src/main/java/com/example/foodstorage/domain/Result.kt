package com.example.foodstorage.domain

sealed class Result<T> {
    data class Success<T>(val result:T ): Result<T>()
    data class Error(val message: String): Result<Nothing>()
}