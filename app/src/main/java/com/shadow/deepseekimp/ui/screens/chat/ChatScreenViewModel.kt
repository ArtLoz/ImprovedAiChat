package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.domain.chat.ChatItemModel
import com.shadow.deepseekimp.domain.chat.SendMessageToAiUseCase
import com.shadow.deepseekimp.domain.chat.SendMessageToAiUseCaseTest
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import com.shadow.deepseekimp.ui.utils.io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val sendMessageToAiUseCase: SendMessageToAiUseCase,
    private val sendMessageToAiUseCase2: SendMessageToAiUseCaseTest,
) : ViewModel() {

    private val _screenModel = MutableStateFlow(
        ChatScreenModel(
            chatItems = listOf(
                ChatItemModel(
                    message = "You are a helpful assistant",
                    author = ChatItemModel.Author.SYSTEM
                )
            )
        )
    )
    val screenModel = _screenModel.asStateFlow()

    fun processIntent(intent: ChatScreenIntent) {
        when (intent) {
            is ChatScreenIntent.OnMessageInput -> _screenModel.update {
                it.copy(
                    inputMessage = intent.msg
                )
            }

            ChatScreenIntent.OnMessageSendClick -> {
                addMeMessageToChat()
                onSendMessageToAi()
            }
        }
    }

    private fun onSendMessageToAi() {
        io {
            sendMessageToAiUseCase2(_screenModel.value.chatItems)
                .onStart {
                    _screenModel.update { it.copy(botWrite = true) }
                }
                .collect { result ->
                    when (result) {
                        is UseCaseResult.Error -> Unit
                        is UseCaseResult.Success -> {
                            addBotMessageToChat(result.model)
                        }
                    }
                }
        }

    }

    private fun addBotMessageToChat(model: ChatItemModel) {
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


    private fun addMeMessageToChat() {
        _screenModel.update {
            it.copy(
                inputMessage = "",
                chatItems = it.chatItems + listOf(
                    ChatItemModel(
                        message = it.inputMessage,
                        author = ChatItemModel.Author.ME
                    )
                )
            )
        }
    }

}