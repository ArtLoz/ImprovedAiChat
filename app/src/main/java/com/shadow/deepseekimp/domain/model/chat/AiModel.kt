package com.shadow.deepseekimp.domain.model.chat

import androidx.compose.ui.graphics.Color
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.ui.theme.colorDeepSeek
import com.shadow.deepseekimp.ui.theme.colorQwen

enum class AiModel(
    val valueNameLocal: Int,
    val valueIcon: Int,
    val iconColor: Color,
    val modelApiName: String,
    val description: Int,
    val descriptionExpanded: Int,
    val title: Int,
) {
    DEEEP_SEEK(
        valueIcon = R.drawable.ic_deepseek,
        valueNameLocal = R.string.aiModel_deep_seek,
        iconColor = colorDeepSeek,
        modelApiName = "deepseek-chat",
        description = R.string.deep_seek_des,
        title = R.string.deep_seek_title,
        descriptionExpanded = R.string.deep_seek_des_exp,

    ),
    QWEN_MAX(
        valueIcon = R.drawable.ic_qwen_logo,
        valueNameLocal = R.string.aiModel_qwen_max,
        iconColor = colorQwen,
        modelApiName = "qwen-max",
        description = R.string.qwen_max_description,
        title = R.string.qwen_max_title,
        descriptionExpanded = R.string.qwen_max_des_exp
    ),
    QWEN_PLUS(
        valueIcon = R.drawable.ic_qwen_logo,
        valueNameLocal = R.string.aiModel_qwen_plus,
        iconColor = colorQwen,
        modelApiName = "qwen-plus",
        description = R.string.qwen_plus_description,
        title = R.string.qwen_plus_title,
        descriptionExpanded = R.string.qwen_plus_des_exp
    )
}