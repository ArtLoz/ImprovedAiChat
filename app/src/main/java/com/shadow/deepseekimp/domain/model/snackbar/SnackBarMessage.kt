package com.shadow.deepseekimp.domain.model.snackbar

sealed interface SnackBarMessage {

    data class ErrorMsg(val message: String) : SnackBarMessage
    data class InfoMsg(val message: String) : SnackBarMessage
}