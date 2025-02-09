package com.shadow.deepseekimp.domain.usecase.chatselector

import com.shadow.deepseekimp.data.repository.AiModelRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import javax.inject.Inject

class SaveCurrentAiModelUseCase @Inject constructor(
    private val aiModelRepository: AiModelRepository
) {

    suspend operator fun invoke(model: AiModel){
        aiModelRepository.saveCurrentAiModel(model)
    }
}