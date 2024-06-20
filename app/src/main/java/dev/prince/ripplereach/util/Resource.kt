package dev.prince.ripplereach.util

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int = 200) : Resource<Nothing>()
}