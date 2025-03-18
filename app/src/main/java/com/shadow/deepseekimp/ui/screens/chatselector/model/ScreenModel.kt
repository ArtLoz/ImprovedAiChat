package com.shadow.deepseekimp.ui.screens.chatselector.model

import com.shadow.deepseekimp.domain.model.chat.AiModel

data class ScreenModel(
    val listAiModel: List<AiModel> = listOf(AiModel.DEEEP_SEEK, AiModel.QWEN_MAX, AiModel.QWEN_PLUS),
    val currentAiModel: AiModel = AiModel.DEEEP_SEEK
)
