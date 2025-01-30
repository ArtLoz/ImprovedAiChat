package com.shadow.deepseekimp.data.repository

import android.util.Log
import com.google.gson.Gson
import com.shadow.deepseekimp.data.database.dao.ChatDao
import com.shadow.deepseekimp.data.network.api.ChatApi
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatRequestDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatRequestDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenMessageDto
import com.shadow.deepseekimp.data.network.utils.toChatItemModel
import com.shadow.deepseekimp.data.network.utils.toMessageDeepDto
import com.shadow.deepseekimp.data.network.utils.toMessageQwenDto
import com.shadow.deepseekimp.domain.chat.ChatItemModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi
) {

    suspend fun sendMessageToAiDeep(listMessage: List<ChatItemModel>): ChatItemModel {
        val request = DeepSeekChatRequestDto(
            messages = listMessage.map { it.toMessageDeepDto() },
        )
        val response = chatApi.sendMessageToDeepSeek(request)
        if (!response.isSuccessful) throw Exception("Response is not successful")
        val body = response.body() ?: throw Exception("Response body is null")
        return body.choices.firstOrNull()?.message?.toChatItemModel(body.id)
            ?: throw Exception("Response choices is null")
    }

    suspend fun sendMessageToAiQwen(listMessage: List<ChatItemModel>): ChatItemModel {
        val request = QwenChatRequestDto(
            messages = listMessage.map { it.toMessageQwenDto() },
        )
        val response = chatApi.sendMessageToQwen(request)
        if (!response.isSuccessful) throw Exception("Response is not successful")
        val body = response.body() ?: throw Exception("Response body is null")
        return body.choices.firstOrNull()?.qwenMessageDto?.toChatItemModel(body.id)
            ?: throw Exception("Response choices is null")
    }


    fun sendMessageToAiQwenStream(listMessage: List<ChatItemModel>): Flow<ChatItemModel> {
        return flow {
            val request = QwenChatRequestDto(
                messages = listMessage.map { it.toMessageQwenDto() },
            )
            val response = chatApi.sendMessageToQwenStream(request).execute()
            if (!response.isSuccessful) throw Exception("Response is not successful")
            val body = response.body() ?: throw Exception("Response body is null")
            body.byteStream().bufferedReader().use { input ->
                while (currentCoroutineContext().isActive) {
                    val line = input.readLine()
                    if (line != null && line.startsWith("data:")) {
                        try {
                            val model = Gson().fromJson(
                                line.substring(5).trim(),
                                QwenChatResponseDto::class.java
                            )
                            val chatModel =
                                model.choices.firstOrNull()?.delta?.toChatItemModel(model.id)
                                    ?: throw Exception("Response choices is null")
                            Log.d("ChatRepository", "ChatModel: $chatModel")
                            emit(chatModel)
                        } catch (e: Exception) {
                            Log.d("ChatRepository", "Error: ${e.message}")
                            break
                        }
                    }
                }
            }
        }
    }
}