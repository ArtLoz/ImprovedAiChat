package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.data.service.workes.WorkerController
import com.shadow.deepseekimp.domain.model.chat.AiAnswerMode
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.model.snackbar.SnackBarMessage
import com.shadow.deepseekimp.domain.usecase.chat.AddChatMessageToHistoryUseCase
import com.shadow.deepseekimp.domain.usecase.chat.SendMessageToAiUseCase
import com.shadow.deepseekimp.domain.usecase.chat.SendMessageToAiUseCaseStream
import com.shadow.deepseekimp.domain.usecase.chatselector.GetCurrentAiModelUseCase
import com.shadow.deepseekimp.domain.utils.SnackBarService
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.ui.baseui.io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OneTimeChatScreenViewModel @Inject constructor(
    private val sendMessageToAiUseCaseStream: SendMessageToAiUseCaseStream,
    private val sendMessageToAiUseCase: SendMessageToAiUseCase,
    private val addChatMessageToHistoryUseCase: AddChatMessageToHistoryUseCase,
    private val getCurrentAiModelUseCase: GetCurrentAiModelUseCase,
    private val snackBarService: SnackBarService,
) : BaseChatViewModel() {

    private suspend fun onSendMessageToAiStream() {
        sendMessageToAiUseCaseStream(_screenModel.value.chatItems, aiModel)
            .onStart {
                _screenModel.update { it.copy(botWrite = true) }
            }
            .onCompletion {
                addChatMessageToHistoryUseCase(_screenModel.value.chatItems.last())
            }
            .collect { result ->
                when (result) {
                    is UseCaseResult.Error -> {
                        _screenModel.update { it.copy(botWrite = false) }
                        snackBarService.showSnackBar(
                            SnackBarMessage.ErrorMsg(
                                result.message
                            )
                        )
                    }

                    is UseCaseResult.Success -> {
                        addBotMessageToChatStream(result.model)
                    }
                }
            }
    }

    init {
        io{
            aiModel = when(val result = getCurrentAiModelUseCase()){
                is UseCaseResult.Success -> {
                    result.model
                }

                is UseCaseResult.Error -> {
                    AiModel.DEEEP_SEEK

                }
            }
            addAiChatPrompt()

        }

    }
    override suspend fun onSendMessageTooAi() {
        _screenModel.update {
            it.copy(botWrite = true)
        }
        when (val useCaseResult = sendMessageToAiUseCase(_screenModel.value.chatItems, aiModel)) {
            is UseCaseResult.Error -> {
                _screenModel.update {
                    it.copy(botWrite = false)
                }
                snackBarService.showSnackBar(
                    SnackBarMessage.ErrorMsg(
                        useCaseResult.message
                    )
                )
            }

            is UseCaseResult.Success -> _screenModel.update {
                it.copy(
                    chatItems = it.chatItems + listOf(useCaseResult.model),
                    botWrite = false
                )
            }
        }
    }

}