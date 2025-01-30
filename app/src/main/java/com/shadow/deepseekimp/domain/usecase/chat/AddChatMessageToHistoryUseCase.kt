package com.shadow.deepseekimp.domain.usecase.chat

import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import javax.inject.Inject

class AddChatMessageToHistoryUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(model:ChatItemModel){
        chatRepository.addChatModel(model)
    }
}