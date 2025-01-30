package com.shadow.deepseekimp.domain.usecase.chat

import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SendMessageToAiUseCaseStream @Inject constructor(
    private val chatRepository: ChatRepository
) {

    operator fun invoke(
        models: List<ChatItemModel>,
        aiModel: AiModel
    ): Flow<UseCaseResult<ChatItemModel>> {
        return if (aiModel == AiModel.DEEEP_SEEK) {
            chatRepository.sendMessageToAiDeepStream(models)
                .map { UseCaseResult.Success(it) as UseCaseResult<ChatItemModel> }
                .catch { e -> emit(UseCaseResult.Error(e.message ?: "Error")) }
        } else {
            chatRepository.sendMessageToAiQwenStream(models)
                .map { UseCaseResult.Success(it) as UseCaseResult<ChatItemModel> }
                .catch { e -> emit(UseCaseResult.Error(e.message ?: "Error")) }
        }
    }
}