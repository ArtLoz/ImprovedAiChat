package com.shadow.deepseekimp.ui.screens.chat.model

import com.shadow.deepseekimp.domain.model.chat.ChatItemModel

data class ChatScreenModel(
    val chatItems: List<ChatItemModel> = emptyList(),
    val botWrite: Boolean = false,
    val inputMessage:String = "",
)