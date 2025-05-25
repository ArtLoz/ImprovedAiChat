package com.shadow.deepseekimp.ui.baseui

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chatselector.PromptModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@Composable
fun HorizontalPagerPrompt(
    modifier: Modifier = Modifier,
    firstCurrentModel: AiModel,
    listPrompt: List<AiModel>,
    onClickElement: () -> Unit,
    currentPage: (Int) -> Unit,
) {
    val horizontalState = rememberPagerState { listPrompt.size }
    LaunchedEffect(horizontalState.currentPage) {
        currentPage(horizontalState.currentPage)
    }
    LaunchedEffect(firstCurrentModel) {
        val index = listPrompt.indexOf(firstCurrentModel)
        horizontalState.animateScrollToPage(index)
    }
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val pageSize = maxWidth * 0.3f
        Column {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = horizontalState,
                contentPadding = PaddingValues(
                    start = (this@BoxWithConstraints.maxWidth - pageSize) / 2,
                    end = (this@BoxWithConstraints.maxWidth - pageSize) / 2
                ),
            ) { page ->
                PromptPagerElement(
                    currentPage = page,
                    pagerState = horizontalState,
                    model = listPrompt[page],
                    onClickElement = onClickElement
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                val verticalState = rememberPagerState { listPrompt.size }
                VerticalPager(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .heightIn(max = (this@BoxWithConstraints.maxHeight * 0.18f)),
                    state = verticalState,
                    userScrollEnabled = false,
                ) { page ->
                    VerticalPageItem(
                        model = listPrompt[page]
                    )
                }
                LaunchedEffect(Unit) {
                    snapshotFlow {
                        Pair(
                            horizontalState.currentPage,
                            horizontalState.currentPageOffsetFraction
                        )
                    }.collect { (page, offset) ->
                        verticalState.scrollToPage(page, offset)
                    }
                }
            }
        }
    }
}

@Composable
fun PromptPagerElement(
    currentPage: Int,
    pagerState: PagerState,
    model: AiModel,
    onClickElement: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val checkColor = if (currentPage == pagerState.currentPage) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    AsyncImage(
        modifier = Modifier
            .graphicsLayer {
                val pageOffset = ((pagerState.currentPage - currentPage) + pagerState
                    .currentPageOffsetFraction).absoluteValue
                val scale = lerp(1f, 1.75f, pageOffset)
                scaleX /= scale
                scaleY /= scale
                alpha = lerp(
                    start = 0.25f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(22.dp))
            .border(2.dp, color = checkColor, shape = RoundedCornerShape(22.dp))
            .clickable {
                scope.launch {
                    if (pagerState.currentPage == currentPage) {
                        onClickElement.invoke()
                    } else pagerState.animateScrollToPage(currentPage)
                }
            }
            .padding(8.dp),
        contentScale = ContentScale.Crop,
        model = model.valueIcon,
        contentDescription = stringResource(R.string.chat_selector_img_prompt)
    )
}

@Composable
fun VerticalPageItem(
    modifier: Modifier = Modifier,
    model: AiModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(model.title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            text = stringResource(model.description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }

}