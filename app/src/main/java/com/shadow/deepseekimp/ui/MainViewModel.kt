package com.shadow.deepseekimp.ui

import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.data.service.RemoveConfigureController
import com.shadow.deepseekimp.data.datastore.DataStoreHelper
import com.shadow.deepseekimp.ui.baseui.io
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val removeConfigureController: RemoveConfigureController,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {
    var loadingRemoteConfig: Boolean = true

    init {
        loadRemoteConfig()
    }

    private fun loadRemoteConfig() {
        removeConfigureController.fetchAndActivate { tokenDeep, tokenQwen ->
            if (tokenDeep != null) {
                io {
                    dataStoreHelper.setTokenForDeepSeek(tokenDeep)
                }
            }
            if (tokenQwen != null) {
                io {
                    dataStoreHelper.setTokenForQwen(tokenQwen)
                }

            }
            loadingRemoteConfig = false
        }
    }
}