package com.shadow.deepseekimp.data.service.workes

import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shadow.deepseekimp.data.service.workes.history.HistoryWorkerRequest
import com.shadow.deepseekimp.data.service.workes.history.HistoryWorkerSender
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.domain.utils.WorkerResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject


class WorkerController @Inject constructor(
    private val workManager: WorkManager
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val currentWorkerState
        get() = workManager.getWorkInfosByTagFlow(HISTORY_WORKER_NAME).map {
            it.find { it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED } != null
        }.stateIn(scope, SharingStarted.Lazily, false)
    val workerInfo
        get() = workManager.getWorkInfosByTagFlow(HISTORY_WORKER_NAME)
            .onEach { workersInfoList ->
                val filter =
                    workersInfoList.filter { it.state == WorkInfo.State.SUCCEEDED || it.state == WorkInfo.State.FAILED }
                if (filter.isNotEmpty()) workManager.pruneWork()
            }
            .map { workersInfoList ->
                val data = workersInfoList.firstOrNull()
                when (data?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val response =
                            data.outputData.getString(HistoryWorkerSender.RESPONSE_KEY)
                                ?: throw Exception("Response from worker null")
                        WorkerResult.Success(HistoryWorkerSender.getWorkerResponseData(response))
                    }

                    WorkInfo.State.FAILED -> {
                        WorkerResult.Error(
                            data.outputData.getString(HistoryWorkerSender.ERROR_KEY)
                                ?: "Worker error response is null"
                        )
                    }

                    null -> null
                    else -> WorkerResult.Working
                }
            }

    init {
        workManager.pruneWork()
    }

    fun runSendWorkerApiRequest(
        listMessage: List<ChatItemModel>,
        model: AiModel,
    ) {
        if (currentWorkerState.value) return
        val requestData =
            HistoryWorkerSender.getWorkerRequestData(HistoryWorkerRequest(listMessage, model))
        val worker = OneTimeWorkRequestBuilder<HistoryWorkerSender>()
            .addTag(HISTORY_WORKER_NAME)
            .setInputData(workDataOf(HistoryWorkerSender.REQUEST_KEY to requestData))
            .build()
        workManager.enqueue(worker)
    }


    private companion object {
        const val HISTORY_WORKER_NAME = "HISTORY_WORKER"
    }
}