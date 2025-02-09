package com.shadow.deepseekimp.domain.model.chat

import androidx.compose.ui.graphics.Color
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.ui.theme.colorDeepSeek
import com.shadow.deepseekimp.ui.theme.colorQwen

enum class AiModel(
    val valueNameLocal: Int,
    val valueIcon: Int,
    val iconColor:Color
) {
    DEEEP_SEEK(
        valueIcon = R.drawable.ic_deepseek,
        valueNameLocal = R.string.aiModel_deep_seek,
        iconColor = colorDeepSeek
    ),
    QWEN_MAX(
        valueIcon = R.drawable.ic_qwen_logo,
        valueNameLocal = R.string.aiModel_qwen_max,
        iconColor = colorQwen
    )
}