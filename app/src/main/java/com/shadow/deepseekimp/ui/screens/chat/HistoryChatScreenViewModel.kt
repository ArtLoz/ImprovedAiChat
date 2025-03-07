package com.shadow.deepseekimp.ui.screens.chat

import com.shadow.deepseekimp.data.service.workes.WorkerController
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.snackbar.SnackBarMessage
import com.shadow.deepseekimp.domain.usecase.chat.AddChatMessageToHistoryUseCase
import com.shadow.deepseekimp.domain.usecase.chat.ClearHistoryChatUseCase
import com.shadow.deepseekimp.domain.usecase.chat.GetHistoryMessageListUseCase
import com.shadow.deepseekimp.domain.usecase.chatselector.GetCurrentAiModelUseCase
import com.shadow.deepseekimp.domain.utils.SnackBarService
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.domain.utils.WorkerResult
import com.shadow.deepseekimp.ui.baseui.io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryChatScreenViewModel @Inject constructor(
    private val getHistoryMessageListUseCase: GetHistoryMessageListUseCase,
    private val clearHistoryChatUseCase: ClearHistoryChatUseCase,
    private val snackBarService: SnackBarService,
    private val workerController: WorkerController,
    private val addChatMessageToHistoryUseCase: AddChatMessageToHistoryUseCase,
    private val getCurrentAiModelUseCase: GetCurrentAiModelUseCase
) : BaseChatViewModel() {

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
            loadChatHistory()
            collectStatusWorker()
            collectInfoFromWorker()

        }

    }
    private suspend fun loadChatHistory() {
        when (val useCaseResult = getHistoryMessageListUseCase(aiModel)) {
            is UseCaseResult.Error -> Unit
            is UseCaseResult.Success -> {
                _screenModel.update {
                    it.copy(
                        chatItems = it.chatItems + useCaseResult.model
                    )
                }
            }
        }
    }

    private fun collectStatusWorker() {
        io {
            workerController.currentWorkerState.collect { state ->
                _screenModel.update { it.copy(botWrite = state) }
            }
        }

    }

    private suspend fun collectInfoFromWorker() {
        workerController.workerInfo.collect { result ->
            when (result) {
                is WorkerResult.Success -> {
                    addBotMessageToChatStream(result.model)
                }

                is WorkerResult.Error -> {
                    snackBarService.showSnackBar(
                        SnackBarMessage.ErrorMsg(
                            result.message
                        )
                    )
                }

                else -> Unit
            }
        }
    }

    fun clearHistory() {
        io {
            clearHistoryChatUseCase(aiModel)
            addAiChatPrompt()
        }
    }

    override suspend fun onSendMessageTooAi() {
        workerController.runSendWorkerApiRequest(aiModel)
    }

    override suspend fun addMeMessageToChat() {
        super.addMeMessageToChat()
        addChatMessageToHistoryUseCase(_screenModel.value.chatItems.last())
    }


}