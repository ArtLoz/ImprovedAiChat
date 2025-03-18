package com.shadow.deepseekimp.domain.usecase.chat

import android.util.Log
import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import kotlinx.coroutines.delay
import javax.inject.Inject

class SendMessageToAiUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(
        models: List<ChatItemModel>,
        aiModel: AiModel
    ): UseCaseResult<ChatItemModel> {
        return try {
            val apiResult =
                if (aiModel == AiModel.DEEEP_SEEK) chatRepository.sendMessageToAiDeep(models, aiModel)
                else chatRepository.sendMessageToAiQwen(models, aiModel)
            UseCaseResult.Success(apiResult)
        } catch (e: Exception) {
            UseCaseResult.Error(e.message ?: "Unknown error")
        }

    }
}