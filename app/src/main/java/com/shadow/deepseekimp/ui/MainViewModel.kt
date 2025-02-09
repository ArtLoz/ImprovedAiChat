package com.shadow.deepseekimp.ui

import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.data.RemoveConfigureController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val removeConfigureController: RemoveConfigureController
): ViewModel() {
    var loadingRemoteConfig: Boolean = true

    init {
        loadRemoteConfig()
    }

    private fun loadRemoteConfig(){
        removeConfigureController.fetchAndActivate {
            loadingRemoteConfig = false
        }
    }
}