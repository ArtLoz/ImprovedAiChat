package com.shadow.deepseekimp.ui.baseui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun ViewModel.io(crossinline suspender: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        suspender()
    }
}
