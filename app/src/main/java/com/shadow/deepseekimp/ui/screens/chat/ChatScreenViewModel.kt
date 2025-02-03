package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.domain.model.chat.AiAnswerMode
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.model.snackbar.SnackBarMessage
import com.shadow.deepseekimp.domain.usecase.chat.AddChatMessageToHistoryUseCase
import com.shadow.deepseekimp.domain.usecase.chat.GetHistoryMessageListUseCase
import com.shadow.deepseekimp.domain.usecase.chat.SendMessageToAiUseCase
import com.shadow.deepseekimp.domain.usecase.chat.SendMessageToAiUseCaseStream
import com.shadow.deepseekimp.domain.utils.SnackBarService
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import com.shadow.deepseekimp.ui.baseui.io
import com.shadow.deepseekimp.ui.nav.ChatType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val sendMessageToAiUseCaseStream: SendMessageToAiUseCaseStream,
    private val sendMessageToAiUseCase: SendMessageToAiUseCase,
    private val addChatMessageToHistoryUseCase: AddChatMessageToHistoryUseCase,
    private val getHistoryMessageListUseCase: GetHistoryMessageListUseCase,
    private val snackBarService: SnackBarService
) : ViewModel() {

    private lateinit var chatType: ChatType
    private lateinit var aiModel: AiModel

    private val _screenModel = MutableStateFlow(ChatScreenModel())
    val screenModel = _screenModel.asStateFlow()

    fun setupChatType(type: ChatType) {
        chatType = type
        aiModel = AiModel.DEEEP_SEEK
        addAiChatPrompt()
    }

    private fun addAiChatPrompt() {
        io {
            _screenModel.update {
                it.copy(
                    chatItems = it.chatItems + listOf(
                        ChatItemModel(
                            message = "You are a helpful assistant.",
                            author = Author.SYSTEM,
                            aiModel = aiModel
                        )
                    )
                )
            }
            if (chatType == ChatType.ONE_TIME) return@io
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

    }

    fun processIntent(intent: ChatScreenIntent) {
        when (intent) {
            is ChatScreenIntent.OnMessageInput -> _screenModel.update {
                it.copy(
                    inputMessage = intent.msg
                )
            }

            ChatScreenIntent.OnMessageSendClick -> {
                io {
                    addMeMessageToChat()
                    when (screenModel.value.aiAnswerMode) {
                        AiAnswerMode.STREAM -> onSendMessageToAiStream()
                        AiAnswerMode.MESSAGE -> onSendMessageToAi()
                    }
                }
            }

            is ChatScreenIntent.AnimationDoneMsg -> {
                _screenModel.update {
                    it.copy(
                        chatItems = it.chatItems.map { chatItem ->
                            if (chatItem.id == intent.id) {
                                chatItem.copy(alreadyAnimated = true)
                            } else {
                                chatItem
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun onSendMessageToAi() {
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

            is UseCaseResult.Success -> addBotMessageToChat(useCaseResult.model)
        }
    }

    private suspend fun addBotMessageToChat(model: ChatItemModel) {

        _screenModel.update {
            it.copy(
                chatItems = it.chatItems + listOf(model),
                botWrite = false
            )
        }
        if (chatType == ChatType.ONE_TIME) return
        addChatMessageToHistoryUseCase(_screenModel.value.chatItems.last())
    }


    private suspend fun onSendMessageToAiStream() {
        sendMessageToAiUseCaseStream(_screenModel.value.chatItems, aiModel)
            .onStart {
                _screenModel.update { it.copy(botWrite = true) }
            }
            .onCompletion {
                if (chatType == ChatType.ONE_TIME) return@onCompletion
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

    private fun addBotMessageToChatStream(model: ChatItemModel) {
        io {
            _screenModel.update { screenState ->
                val updatedChatItems = screenState.chatItems.toMutableList().apply {
                    val index = indexOfFirst { it.id == model.id }
                    if (index != -1) {
                        this[index] = this[index].copy(
                            message = this[index].message + model.message,
                            time = model.time
                        )
                    } else {
                        add(model)
                    }
                }
                screenState.copy(chatItems = updatedChatItems, botWrite = false)
            }
        }
    }

    private suspend fun addMeMessageToChat() {
        val model = ChatItemModel(
            message = _screenModel.value.inputMessage,
            author = Author.ME,
            aiModel = aiModel
        )
        _screenModel.update {
            it.copy(
                inputMessage = "",
                chatItems = it.chatItems + listOf(model)
            )
        }
        if (chatType == ChatType.ONE_TIME) return
        addChatMessageToHistoryUseCase(model)

    }
}