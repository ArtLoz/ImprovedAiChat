package com.shadow.deepseekimp.ui.baseui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.domain.model.chat.AiModel
import kotlin.coroutines.CoroutineContext

@Composable
fun AiModelContext(
    modifier: Modifier = Modifier,
    listAiModel: List<AiModel>,
    onElementClick: (AiModel) -> Unit
) {
    Card(modifier = modifier) {
        repeat(listAiModel.size){
            AiModelUi(
                aiModel = listAiModel[it],
                onElementClick = onElementClick
            )
        }
    }
}

@Composable
fun AiModelUi(
    aiModel: AiModel,
    onElementClick: (AiModel) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onElementClick(aiModel) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp),
            painter = painterResource(aiModel.valueIcon),
            tint = aiModel.iconColor,
            contentDescription = null
        )
        Text(
            text = stringResource(aiModel.valueNameLocal),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}