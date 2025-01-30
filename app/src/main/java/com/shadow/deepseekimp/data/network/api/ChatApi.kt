package com.shadow.deepseekimp.data.network.api

import com.shadow.deepseekimp.BuildConfig
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatRequestDto
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatResponseDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatRequestDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface ChatApi {

    @POST("chat/completions")
    suspend fun sendMessageToDeepSeek(
        @Body model: DeepSeekChatRequestDto
    ): Response<DeepSeekChatResponseDto>

    @POST("${BuildConfig.BASE_URL_QWEN}compatible-mode/v1/chat/completions")
    suspend fun sendMessageToQwen(
        @Body model: QwenChatRequestDto
    ): Response<QwenChatResponseDto>

    @Streaming
    @POST("${BuildConfig.BASE_URL_QWEN}compatible-mode/v1/chat/completions")
     fun sendMessageToQwenStream(
        @Body model: QwenChatRequestDto
    ): Call<ResponseBody>
}