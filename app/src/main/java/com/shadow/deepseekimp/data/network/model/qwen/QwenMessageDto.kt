package com.shadow.deepseekimp.data.network.model.qwen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class QwenMessageDto(
    val content: String,
    val role: String
)