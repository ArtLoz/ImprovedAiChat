package com.shadow.deepseekimp.domain.model.chat

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ChatItemModel(
    val id: String = generateChatId(),
    val message: String,
    val author: Author,
    val time: Long = System.currentTimeMillis(),
    val aiModel:AiModel,
    val alreadyAnimated:Boolean = false
) {

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generateChatId(): String {
            return "chatcmpl-${Uuid.random()}"
        }
    }
}
