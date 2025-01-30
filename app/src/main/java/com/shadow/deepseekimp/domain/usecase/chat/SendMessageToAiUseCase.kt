package com.shadow.deepseekimp.domain.usecase.chat

import android.util.Log
import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import javax.inject.Inject

class SendMessageToAiUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(models: List<ChatItemModel>): UseCaseResult<ChatItemModel> {
        return try {
            val apiResult = chatRepository.sendMessageToAiQwen(models)
            UseCaseResult.Success(apiResult)
        } catch (e: Exception) {
            Log.d("SendMessageToAiUseCase", "Error: ${e.message}")
            UseCaseResult.Error(e.message ?: "Unknown error")
        }

    }
}