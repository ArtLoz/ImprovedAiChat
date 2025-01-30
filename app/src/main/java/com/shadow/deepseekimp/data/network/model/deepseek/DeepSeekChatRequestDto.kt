package com.shadow.deepseekimp.data.network.model.deepseek

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DeepSeekChatRequestDto(
    @SerialName("messages")
    val messages: List<DeepSeekMessageDto>,
    @SerialName("model")
    val model: String = "deepseek-chat",
    val stream:Boolean
)