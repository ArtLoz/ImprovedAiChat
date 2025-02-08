package com.shadow.deepseekimp.data.service.workes.history

import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel

data class HistoryWorkerRequest(
    val listMessage:List<ChatItemModel>,
    val aiModel: AiModel
)
