package com.shadow.deepseekimp.ui.screens.chatselector

import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.data.repository.AiModelRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.usecase.chatselector.GetCurrentAiModelUseCase
import com.shadow.deepseekimp.domain.usecase.chatselector.SaveCurrentAiModelUseCase
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.ui.baseui.io
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chatselector.model.ChatSelectorIntent
import com.shadow.deepseekimp.ui.screens.chatselector.model.ScreenModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScreenChatSelectorViewModel @Inject constructor(
    private val getCurrentAiModelUseCase: GetCurrentAiModelUseCase,
    private val saveCurrentAiModelUseCase: SaveCurrentAiModelUseCase
) : ViewModel() {

    private val _screenModel = MutableStateFlow(ScreenModel())
    val screenModel = _screenModel.asStateFlow()

    init {
        io {
            when (val result = getCurrentAiModelUseCase()) {
                is UseCaseResult.Success -> _screenModel.update { it.copy(currentAiModel = result.model) }
                else -> Unit
            }
        }
    }

    fun processIntent(intent: ChatSelectorIntent) {
        when (intent) {
            is ChatSelectorIntent.SelectAiModel -> swapAiModel(intent.aiModel)
        }
    }

    private fun swapAiModel(aiModel: AiModel) {
        io {
            _screenModel.update {
                it.copy(
                    currentAiModel = aiModel
                )
            }
            saveCurrentAiModelUseCase(aiModel)
        }
    }
}