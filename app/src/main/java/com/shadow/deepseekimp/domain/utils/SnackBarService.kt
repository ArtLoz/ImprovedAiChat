package com.shadow.deepseekimp.domain.utils

import androidx.compose.material3.SnackbarHostState
import com.shadow.deepseekimp.domain.model.snackbar.SnackBarMessage

interface SnackBarService {

    fun getSnackBarHost(): SnackbarHostState

    fun showSnackBar(message: SnackBarMessage)
}