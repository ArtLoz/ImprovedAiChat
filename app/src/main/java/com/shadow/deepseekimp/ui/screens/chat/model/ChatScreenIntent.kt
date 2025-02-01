package com.shadow.deepseekimp.ui.screens.chat.model

sealed interface ChatScreenIntent {

    data class OnMessageInput(val msg: String) : ChatScreenIntent
    data object OnMessageSendClick : ChatScreenIntent
    data class AnimationDoneMsg(val id: String) : ChatScreenIntent
}