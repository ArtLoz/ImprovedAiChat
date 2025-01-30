package com.shadow.deepseekimp.domain.usecase.chat

import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import javax.inject.Inject

class GetHistoryMessageListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(aiModel: AiModel): UseCaseResult<List<ChatItemModel>> {
        return UseCaseResult.Success(chatRepository.getChatHistory(aiModel))
    }
}