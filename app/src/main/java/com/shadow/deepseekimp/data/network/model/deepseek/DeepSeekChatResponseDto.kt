package com.shadow.deepseekimp.data.network.model.deepseek

import com.google.gson.annotations.SerializedName
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto.Delta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class DeepSeekChatResponseDto(
    @SerialName("choices")
    val choices: List<Choice>,
    @SerialName("created")
    val created: Int,
    @SerialName("id")
    val id: String,
    @SerialName("model")
    val model: String,
){
    data class Choice(
        @SerialName("finish_reason")
        val finishReason: String,
        @SerialName("index")
        val index: Int,
        @SerialName("message")
        val message: DeepSeekMessageDto,
        @SerializedName("delta")
        val delta: Delta?,
    )

    data class Delta(
        @SerializedName("content")
        val content: String,
        @SerializedName("role")
        val role: String
    )
}