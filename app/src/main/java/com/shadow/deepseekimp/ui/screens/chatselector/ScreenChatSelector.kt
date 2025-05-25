package com.shadow.deepseekimp.ui.screens.chatselector

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.shadow.deepseekimp.ui.nav.NavScreens
import com.shadow.deepseekimp.ui.nav.NavigationController
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.ui.baseui.AiModelContext
import com.shadow.deepseekimp.ui.baseui.HorizontalPagerPrompt
import com.shadow.deepseekimp.ui.baseui.MainButton
import com.shadow.deepseekimp.ui.baseui.MainButtonOutlined
import com.shadow.deepseekimp.ui.baseui.custompopup.CustomPopup
import com.shadow.deepseekimp.ui.baseui.custompopup.PopupState
import com.shadow.deepseekimp.ui.screens.chatselector.model.ChatSelectorIntent
import com.shadow.deepseekimp.ui.screens.chatselector.model.ScreenModel
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_HISTORY
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_ONE
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes

@Composable
fun ScreenChatSelector(
    modifier: Modifier = Modifier,
    viewModel: ScreenChatSelectorViewModel = hiltViewModel(),
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val screenModel = viewModel.screenModel.collectAsState().value
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Crossfade(
            modifier = Modifier
                .padding(top = (screenHeight * 0.25f).dp)
                .align(Alignment.TopCenter),
            targetState = screenModel.currentAiModel.valueIcon,
            animationSpec = tween(1800)
        ) { icon ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.1f)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = (screenHeight * 0.3f).dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.chat_selector_choose_chat),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.fillMaxWidth(0.6f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(screenModel.currentAiModel.valueIcon),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(
                            R.string.chat_selector_by_model,
                            stringResource(screenModel.currentAiModel.valueNameLocal)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                with(sharedTransitionScope) {
                    MainButton(
                        modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(SHARED_TITLE_KEY_ONE),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        buttonText = stringResource(R.string.chat_selector_one_chat)
                    ) {
                        navigationController.navigate(NavScreens.OneTimeChatScreen)
                    }
                    MainButtonOutlined(
                        modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(SHARED_TITLE_KEY_HISTORY),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        buttonText = stringResource(R.string.chat_selector_history)
                    ) {
                        navigationController.navigate(NavScreens.HistoryChatScreen)
                    }
                }
            }

        }

        HorizontalPagerPrompt(
            modifier = Modifier.align(Alignment.BottomCenter),
            listPrompt = screenModel.listAiModel,
            firstCurrentModel = screenModel.firstInitModel,
            onClickElement = {viewModel.processIntent(ChatSelectorIntent.ShowDetailInfo)},
            currentPage = {
                viewModel.processIntent(ChatSelectorIntent.SelectAiModel(screenModel.listAiModel[it]))
            }
        )
        if(screenModel.showDetailInfo){
            BottomFullDescription(
                currentAiModel = screenModel.currentAiModel,
                onDismiss = { viewModel.processIntent(ChatSelectorIntent.HideDetailInfo) }
            )
        }
    }
}
@Composable
fun BottomFullDescription(
    modifier: Modifier = Modifier,
    currentAiModel: AiModel,
    onDismiss: () -> Unit
){
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = state,
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp)) {
            Text(
                text = stringResource(currentAiModel.title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = Modifier.padding(vertical = 14.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(currentAiModel.descriptionExpanded),
            )
        }
    }
}