package com.shadow.deepseekimp.data.network.utils

import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatResponseDto
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekMessageDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenMessageDto
import com.shadow.deepseekimp.domain.chat.ChatItemModel

fun ChatItemModel.toMessageDeepDto(): DeepSeekMessageDto {
    return DeepSeekMessageDto(
        content = message,
        role = this.author.value
    )
}

fun ChatItemModel.toMessageQwenDto(): QwenMessageDto {
    return QwenMessageDto(
        content = message,
        role = this.author.value
    )
}

fun DeepSeekMessageDto.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id = id,
        message = content,
        author = ChatItemModel.Author.fromValue(role)
            ?: throw IllegalArgumentException("Unknown author")
    )
}

fun QwenMessageDto.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id = id,
        message = content,
        author = ChatItemModel.Author.fromValue(role)
            ?: throw IllegalArgumentException("Unknown author")
    )
}

fun QwenChatResponseDto.Delta.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id  = id,
        message = content,
        author = ChatItemModel.Author.BOT
    )
}
fun DeepSeekChatResponseDto.Delta.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id  = id,
        message = content,
        author = ChatItemModel.Author.BOT
    )
}