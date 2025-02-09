package com.shadow.deepseekimp.domain.usecase.chatselector

import com.shadow.deepseekimp.data.repository.AiModelRepository
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import javax.inject.Inject

class GetCurrentAiModelUseCase @Inject constructor(
    private val aiModelRepository: AiModelRepository
) {

    suspend operator fun invoke(): UseCaseResult<AiModel>{
        return UseCaseResult.Success(aiModelRepository.getCurrentAiModel())
    }
}