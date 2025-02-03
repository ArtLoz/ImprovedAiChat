package com.shadow.deepseekimp.data.repository

import android.util.Log
import com.google.gson.Gson
import com.shadow.deepseekimp.data.database.dao.ChatDao
import com.shadow.deepseekimp.data.database.utils.toAiDbModel
import com.shadow.deepseekimp.data.database.utils.toChatItemModel
import com.shadow.deepseekimp.data.database.utils.toDbModel
import com.shadow.deepseekimp.data.network.api.ChatApi
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatRequestDto
import com.shadow.deepseekimp.data.network.model.deepseek.DeepSeekChatResponseDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatRequestDto
import com.shadow.deepseekimp.data.network.model.qwen.QwenChatResponseDto
import com.shadow.deepseekimp.data.network.utils.toChatItemModel
import com.shadow.deepseekimp.data.network.utils.toMessageDeepDto
import com.shadow.deepseekimp.data.network.utils.toMessageQwenDto
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import kotlinx.coroutines.currentCoroutineContext
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
            stream = false
        )
        val response = chatApi.sendMessageToDeepSeek(request)
        if (!response.isSuccessful) throw Exception("Response is not successful")
        val body = response.body() ?: throw Exception("Response body is null")
        return body.choices.firstOrNull()?.message?.toChatItemModel(body.id)
            ?: throw Exception("Response choices is null")
    }

    fun sendMessageToAiDeepStream(listMessage: List<ChatItemModel>): Flow<ChatItemModel> {
        return flow {
            val request = DeepSeekChatRequestDto(
                messages = listMessage.map { it.toMessageDeepDto() },
                stream = true
            )
            val response = chatApi.sendMessageToDeepSeekStream(request).execute()
            if (!response.isSuccessful) throw Exception("Response is not successful")
            val body = response.body() ?: throw Exception("Response body is null")
            body.byteStream().bufferedReader().use { input ->
                while (currentCoroutineContext().isActive) {
                    val line = input.readLine()
                    if (line != null && line.startsWith("data:")) {
                        try {
                            val model = Gson().fromJson(
                                line.substring(5).trim(),
                                DeepSeekChatResponseDto::class.java
                            )
                            Log.d("ChatRepository", "Model: $model")
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

    suspend fun sendMessageToAiQwen(listMessage: List<ChatItemModel>): ChatItemModel {
        val request = QwenChatRequestDto(
            messages = listMessage.map { it.toMessageQwenDto() },
            stream = false
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
                stream = true
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

    suspend fun addChatModel(chatModel: ChatItemModel) {
        chatDao.insertChatMessage(chatModel.toDbModel())
    }

    suspend fun getChatHistory(aiModel: AiModel):List<ChatItemModel>{
        return chatDao.getAllChatMessages(aiModel = aiModel.toAiDbModel().name).map { it.toChatItemModel() }
    }

    suspend fun clearChatHistory(aiModel: AiModel){
        chatDao.deleteChatMessageByAiModel(aiModel.toAiDbModel().name)
    }
}