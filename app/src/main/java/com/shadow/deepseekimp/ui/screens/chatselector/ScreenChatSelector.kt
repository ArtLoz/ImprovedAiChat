package com.shadow.deepseekimp.ui.screens.chatselector

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shadow.deepseekimp.ui.nav.NavScreens
import com.shadow.deepseekimp.ui.nav.NavigationController
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.ui.baseui.AiModelContext
import com.shadow.deepseekimp.ui.baseui.MainButton
import com.shadow.deepseekimp.ui.baseui.MainButtonOutlined
import com.shadow.deepseekimp.ui.baseui.custompopup.CustomPopup
import com.shadow.deepseekimp.ui.baseui.custompopup.PopupState
import com.shadow.deepseekimp.ui.screens.chatselector.model.ChatSelectorIntent
import com.shadow.deepseekimp.ui.screens.chatselector.model.ScreenModel
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_HISTORY
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_ONE

@Composable
fun ScreenChatSelector(
    modifier: Modifier = Modifier,
    viewModel: ScreenChatSelectorViewModel = hiltViewModel(),
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val screenModel = viewModel.screenModel.collectAsState().value
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Crossfade( targetState = screenModel.currentAiModel.valueIcon,
            animationSpec = tween(1800)
        ) { icon ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.1f)
                )
            }
        }

        AiModelController(
            modifier = Modifier.align(Alignment.TopEnd),
            screenModel = screenModel,
            onElementClick = { viewModel.processIntent(ChatSelectorIntent.SelectAiModel(it)) }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
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
                        painter = painterResource(R.drawable.ic_deepseek),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.chat_selector_by_model, stringResource(screenModel.currentAiModel.valueNameLocal)),
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
    }
}

@Composable
fun AiModelController(
    modifier: Modifier = Modifier,
    screenModel: ScreenModel,
    onElementClick: (AiModel) -> Unit
){
    val popupState: PopupState = remember {
        PopupState(false, )
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { popupState.isVisible = true }
            .padding(horizontal = 14.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(screenModel.currentAiModel.valueIcon),
            contentDescription = null,
            tint = screenModel.currentAiModel.iconColor
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(screenModel.currentAiModel.valueNameLocal),
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        CustomPopup(popupState = popupState,
            offset = DpOffset(0.dp, 14.dp),
            onDismissRequest = { popupState.isVisible  = false }) {
            AiModelContext(
                listAiModel = screenModel.listAiModel,
                onElementClick = {
                    popupState.isVisible = false
                    onElementClick.invoke(it)
                }
            )
        }

    }
}