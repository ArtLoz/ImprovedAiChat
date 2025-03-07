package com.shadow.deepseekimp.data.service.workes.history

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.usecase.chat.AddChatMessageToHistoryUseCase
import com.shadow.deepseekimp.domain.usecase.chat.GetHistoryMessageListUseCase
import com.shadow.deepseekimp.domain.usecase.chat.SendMessageToAiUseCase
import com.shadow.deepseekimp.domain.utils.UseCaseResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class HistoryWorkerSender @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val sendMessageToAiUseCase: SendMessageToAiUseCase,
    private val addChatMessageToHistoryUseCase: AddChatMessageToHistoryUseCase,
    private val getHistoryMessageListUseCase: GetHistoryMessageListUseCase,
) : CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        val requestJson = inputData.getString(REQUEST_KEY) ?: return Result.failure(
            workDataOf(
                ERROR_KEY to "Request is null"
            )
        )
        val requestModel = getRequestModel(requestJson) ?: return Result.failure(
            workDataOf(
                ERROR_KEY to "Gson error"
            )
        )
        val result = sendMessageToAiUseCase(
            getCurrentMessageListFormModel(requestModel.aiModel),
            requestModel.aiModel
        )
        when (result) {
            is UseCaseResult.Error -> return Result.failure(workDataOf(ERROR_KEY to result.message))
            is UseCaseResult.Success -> return handleSuccess(result.model)
        }
    }

    private suspend fun handleSuccess(result: ChatItemModel): Result {
        addChatMessageToHistoryUseCase(result)
        return createWorkerResultData(result)
    }

    private fun getRequestModel(requestJson: String): HistoryWorkerRequest? {
        return try {
            Gson().fromJson(requestJson, HistoryWorkerRequest::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    private fun createWorkerResultData(result: ChatItemModel): Result {
        val stringData = Gson().toJson(result)
        return Result.success(workDataOf(RESPONSE_KEY to stringData))
    }

    private suspend fun getCurrentMessageListFormModel(model: AiModel): List<ChatItemModel> {
        return when (val useCaseResult = getHistoryMessageListUseCase(model)) {
            is UseCaseResult.Error -> emptyList()
            is UseCaseResult.Success -> {
                useCaseResult.model
                getCurrentPrompt(aiModel = model) + useCaseResult.model
            }
        }
    }

    private fun getCurrentPrompt(aiModel: AiModel): List<ChatItemModel> {
        return listOf(
            ChatItemModel(
                message = "You are a helpful assistant.",
                author = Author.SYSTEM,
                aiModel = aiModel
            )
        )
    }


    companion object {
        fun getWorkerRequestData(model: HistoryWorkerRequest): String {
            return Gson().toJson(model)
        }

        @Throws(JsonSyntaxException::class)
        fun getWorkerResponseData(data: String): ChatItemModel {
            return Gson().fromJson(data, ChatItemModel::class.java)
        }

        const val REQUEST_KEY = "request_key"
        const val RESPONSE_KEY = "response_key"
        const val ERROR_KEY = "error_key"
    }
}