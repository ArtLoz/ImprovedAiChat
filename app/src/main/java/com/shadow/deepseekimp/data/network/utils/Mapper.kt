package com.shadow.deepseekimp.data.network.utils

import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatResponseDto
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekMessageDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenMessageDto
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel

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
        author = Author.fromValue(role)
            ?: throw IllegalArgumentException("Unknown author"),
        aiModel = AiModel.DEEEP_SEEK
    )
}

fun QwenMessageDto.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id = id,
        message = content,
        author = Author.fromValue(role)
            ?: throw IllegalArgumentException("Unknown author"),
        aiModel = AiModel.QWEN_MAX
    )
}

fun QwenChatResponseDto.Delta.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id  = id,
        message = content,
        author = Author.BOT,
        aiModel = AiModel.QWEN_MAX
    )
}
fun DeepSeekChatResponseDto.Delta.toChatItemModel(id:String): ChatItemModel {
    return ChatItemModel(
        id  = id,
        message = content,
        author = Author.BOT,
        aiModel = AiModel.DEEEP_SEEK
    )
}