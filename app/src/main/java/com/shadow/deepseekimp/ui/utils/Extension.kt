package com.shadow.deepseekimp.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun ViewModel.io(crossinline suspender: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        suspender()
    }
}