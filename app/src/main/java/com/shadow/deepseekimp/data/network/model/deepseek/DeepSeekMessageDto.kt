package com.shadow.deepseekimp.data.network.model.deepseek

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeepSeekMessageDto(
    @SerialName("content")
    val content: String,
    @SerialName("role")
    val role: String
)