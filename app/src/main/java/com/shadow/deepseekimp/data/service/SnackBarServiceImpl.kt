package com.shadow.deepseekimp.data.service

import androidx.compose.material3.SnackbarHostState
import com.shadow.deepseekimp.domain.model.snackbar.SnackBarMessage
import com.shadow.deepseekimp.domain.utils.SnackBarService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SnackBarServiceImpl : SnackBarService {

    private val job  = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val snackBarHostState = SnackbarHostState()

    override fun getSnackBarHost(): SnackbarHostState {
        return snackBarHostState
    }

    override fun showSnackBar(message: SnackBarMessage) {
        scope.launch {
            when(message){
                is SnackBarMessage.ErrorMsg -> snackBarHostState.showSnackbar(message.message)
                is SnackBarMessage.InfoMsg -> snackBarHostState.showSnackbar(message.message)
            }
        }
    }
}