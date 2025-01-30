package com.shadow.deepseekimp.data.network.model.qwen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class QwenChatRequestDto(
    val messages: List<QwenMessageDto>,
    val model: String = "qwen-max",
    val stream: Boolean = true
)