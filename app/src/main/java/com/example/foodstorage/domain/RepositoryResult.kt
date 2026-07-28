package com.example.foodstorage.domain

sealed class RepositoryResult<out T> {
    data class Success<T>(val result: T): RepositoryResult<T>()
    data class Error(val message: String): RepositoryResult<Nothing>()
}