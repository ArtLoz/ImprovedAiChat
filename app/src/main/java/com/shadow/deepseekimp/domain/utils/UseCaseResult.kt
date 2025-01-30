package com.shadow.deepseekimp.domain.utils

sealed interface UseCaseResult<out T> {
    data class Success<T>(val model: T) : UseCaseResult<T>
    data class Error(val message: String) : UseCaseResult<Nothing>
}