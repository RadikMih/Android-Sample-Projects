package com.radikmih.stockmarketapp.util

sealed class ResultUtil<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): ResultUtil<T>(data)
    class Error<T>(message: String?, data: T? = null): ResultUtil<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true): ResultUtil<T>(null)
}