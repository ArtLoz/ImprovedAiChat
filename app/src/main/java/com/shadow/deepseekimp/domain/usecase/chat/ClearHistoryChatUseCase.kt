package com.shadow.deepseekimp.domain.usecase.chat

import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import javax.inject.Inject

class ClearHistoryChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(aiModel: AiModel){
        chatRepository.clearChatHistory(aiModel)
    }
}