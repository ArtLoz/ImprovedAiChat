package com.shadow.deepseekimp.ui.screens.chatselector.model

import com.shadow.deepseekimp.domain.model.chat.AiModel

sealed interface ChatSelectorIntent {
    data class SelectAiModel(val aiModel: AiModel) : ChatSelectorIntent
    data object HideDetailInfo : ChatSelectorIntent
    data object ShowDetailInfo : ChatSelectorIntent
}