package com.shadow.deepseekimp.ui.screens.chat

import androidx.lifecycle.ViewModel
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import com.shadow.deepseekimp.ui.baseui.io
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseChatViewModel : ViewModel() {
    protected lateinit var aiModel: AiModel

    protected fun addAiChatPrompt() {
        _screenModel.update {
            it.copy(
                chatItems = listOf(
                    ChatItemModel(
                        message = "You are a helpful assistant.",
                        author = Author.SYSTEM,
                        aiModel = aiModel
                    )
                )
            )
        }
    }

    protected val _screenModel = MutableStateFlow(ChatScreenModel())
    val screenModel = _screenModel.asStateFlow()

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
                    onSendMessageTooAi()
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
    protected fun addBotMessageToChatStream(model: ChatItemModel) {
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

    protected abstract suspend fun onSendMessageTooAi()

    protected open suspend fun addMeMessageToChat() {
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
    }
}