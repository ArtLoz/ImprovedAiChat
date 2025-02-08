package com.shadow.deepseekimp.domain.utils

sealed interface WorkerResult<out T>  {
    data class Success<T>(val model: T) : WorkerResult<T>
    data class Error(val message: String) : WorkerResult<Nothing>
    data object Working : WorkerResult<Nothing>
}