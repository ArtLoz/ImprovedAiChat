package com.shadow.deepseekimp.domain.chat

import android.util.Log
import com.shadow.deepseekimp.data.repository.ChatRepository
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SendMessageToAiUseCaseTest @Inject constructor(
    private val chatRepository: ChatRepository
) {

    operator fun invoke(models: List<ChatItemModel>): Flow<UseCaseResult<ChatItemModel>> {
        return chatRepository.sendMessageToAiQwenStream(models)
            .map { UseCaseResult.Success(it) as UseCaseResult<ChatItemModel> }
            .catch { e -> emit(UseCaseResult.Error(e.message ?: "Error")) }
    }
}