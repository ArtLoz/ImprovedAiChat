package com.shadow.deepseekimp.domain.chat

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ChatItemModel(
    val id: String = generateChatId(),
    val message: String,
    val author: Author,
    val time: Long = System.currentTimeMillis()
) {
    enum class Author(val value: String) {
        ME("user"),
        BOT("assistant"),
        SYSTEM("system");

        companion object {
            fun fromValue(value: String): Author? {
                return entries.firstOrNull { it.value == value }
            }
        }
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generateChatId(): String {
            return "chatcmpl-${Uuid.random()}"
        }
    }
}
