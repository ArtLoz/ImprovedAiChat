package com.shadow.deepseekimp.data.network.model.qwen

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class QwenChatResponseDto(
    @SerializedName("choices")
    val choices: List<Choice>,
    @SerializedName("created")
    val created: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("model")
    val model: String,
){
    data class Choice(
        @SerializedName("finish_reason")
        val finishReason: String,
        @SerializedName("index")
        val index: Int,
        @SerializedName("message")
        val qwenMessageDto: QwenMessageDto?,
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