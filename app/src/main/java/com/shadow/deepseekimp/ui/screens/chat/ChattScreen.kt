package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.ui.nav.NavigationController
import com.shadow.deepseekimp.ui.baseui.ChatAiMessage
import com.shadow.deepseekimp.ui.baseui.ChatAiMessageAnimation
import com.shadow.deepseekimp.ui.baseui.ChatInput
import com.shadow.deepseekimp.ui.baseui.ChatUserMessage
import com.shadow.deepseekimp.ui.baseui.ConfirmDialog
import com.shadow.deepseekimp.ui.baseui.fadeOut400delay
import com.shadow.deepseekimp.ui.baseui.isScrolledToTheEnd
import com.shadow.deepseekimp.ui.baseui.keyboardAsState
import com.shadow.deepseekimp.ui.baseui.rememberLastVisibleItemKey
import com.shadow.deepseekimp.ui.baseui.scrollToEnd
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_HISTORY
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_ONE
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes

@Composable
fun OneTimeChatScreen(
    modifier: Modifier = Modifier,
    viewModel: OneTimeChatScreenViewModel = hiltViewModel(),
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val screenModel = viewModel.screenModel.collectAsState().value
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        ChatBar(
            chatName = stringResource(R.string.chat_selector_one_chat),
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
            animationKey = SHARED_TITLE_KEY_ONE,
            onCloseClick = null
        )
        ChatComponent(
            modifier = Modifier
                .padding(horizontal = 14.dp),
            screenModel = screenModel,
            onValueChange = { viewModel.processIntent(ChatScreenIntent.OnMessageInput(it)) },
            onSendClick = { viewModel.processIntent(ChatScreenIntent.OnMessageSendClick) },
            animationDone = { viewModel.processIntent(ChatScreenIntent.AnimationDoneMsg(it)) }
        )

    }
}

@Composable
fun HistoryChatScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryChatScreenViewModel = hiltViewModel(),
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val screenModel = viewModel.screenModel.collectAsState().value
    val showClearDialog = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        ChatBar(
            chatName = stringResource(R.string.chat_selector_history),
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
            animationKey = SHARED_TITLE_KEY_HISTORY,
            onCloseClick = { showClearDialog.value = true }
        )
        ChatComponent(
            modifier = Modifier
                .padding(horizontal = 14.dp),
            screenModel = screenModel,
            useLoader = true,
            onValueChange = { viewModel.processIntent(ChatScreenIntent.OnMessageInput(it)) },
            onSendClick = { viewModel.processIntent(ChatScreenIntent.OnMessageSendClick) },
            animationDone = { viewModel.processIntent(ChatScreenIntent.AnimationDoneMsg(it)) }
        )

    }
    AnimatedVisibility(
        visible = showClearDialog.value,
    ) {
        ConfirmDialog(
            onDismissRequest = { showClearDialog.value = false },
            dialogText = stringResource(R.string.chat_history_clear),
            onConfirm = viewModel::clearHistory
        )
    }

}

@Composable
fun ChatBar(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    chatName: String,
    animationKey: String,
    onCloseClick: (() -> Unit)? = null
) {
    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .padding(8.dp),
            contentAlignment = androidx.compose.ui.Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(animationKey),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                    text = chatName,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            onCloseClick?.let {
                Icon(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.CenterEnd)
                        .clickable(onClick = onCloseClick),
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.cd_button_clear_chat)
                )
            }
        }

    }
}

@Composable
fun ChatComponent(
    modifier: Modifier = Modifier,
    screenModel: ChatScreenModel,
    useLoader: Boolean = false,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    animationDone: (String) -> Unit
) {
    val lazyScreenState = rememberLazyListState()
    var lastCurrentChatItem by remember { mutableIntStateOf(0) }
    var loaderVisibility by remember { mutableStateOf(true) }
    val isKeyboardOpen by keyboardAsState()
    LaunchedEffect(Unit) {
        delay(600)
        loaderVisibility = false
    }

    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = lazyScreenState,
                modifier = Modifier.fillMaxSize()
            ) {

                items(items = screenModel.chatItems, key = { item -> item.id }) { item ->
                    when (item.author) {
                        Author.ME -> ChatUserMessage(message = item.message)
                        Author.BOT -> ChatAiMessage(
                            message = item.message,
                            showAnimation = !item.alreadyAnimated
                        ) {
                            animationDone(item.id)
                        }

                        Author.SYSTEM -> Unit
                    }
                }
                if (screenModel.botWrite) {
                    item {
                        ChatAiMessageAnimation()
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(0.dp))
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = loaderVisibility && useLoader,
                exit = fadeOut400delay()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                )
            }
        }
        ChatInput(
            modifier = Modifier
                .fillMaxWidth(),
            value = screenModel.inputMessage,
            onValueChange = onValueChange,
            onSendClick = onSendClick,
            lock = screenModel.botWrite
        )
    }
    LaunchedEffect(key1 = screenModel.chatItems, key2 = isKeyboardOpen) {
        if (screenModel.chatItems.lastOrNull()?.author == Author.ME && !isKeyboardOpen) {
            lazyScreenState.scrollToEnd()
        }
        if (lazyScreenState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == screenModel.chatItems.lastIndex) {
            lazyScreenState.scrollToItem(lazyScreenState.layoutInfo.totalItemsCount)
        }
        if (screenModel.chatItems.size - lastCurrentChatItem > 2) {
            delay(400)
            if (!lazyScreenState.isScrolledToTheEnd()) {
                lazyScreenState.scrollToEnd()
            }
        }
        lastCurrentChatItem = screenModel.chatItems.size
    }

}