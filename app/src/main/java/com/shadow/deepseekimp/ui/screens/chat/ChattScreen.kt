package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.ui.nav.NavigationController
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel
import com.shadow.deepseekimp.ui.baseui.ChatAiMessage
import com.shadow.deepseekimp.ui.baseui.ChatAiMessageAnimation
import com.shadow.deepseekimp.ui.baseui.ChatInput
import com.shadow.deepseekimp.ui.baseui.ChatUserMessage
import com.shadow.deepseekimp.ui.nav.ChatType
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_HISTORY

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatScreenViewModel = hiltViewModel(),
    chatType: ChatType,
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    animationKey:String
) {
    val screenModel = viewModel.screenModel.collectAsState().value
    Column(
        modifier = modifier
            .imePadding()
            .fillMaxHeight()
    ) {
        LaunchedEffect(Unit) { viewModel.setupChatType(chatType) }
        ChatBar(
            chatName = stringResource(chatType.value),
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
            animationKey = animationKey
        )
        ChatComponent(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .padding(bottom = 14.dp)
                .weight(1f),
            screenModel = screenModel
        )
        ChatInput(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth(),
            value = screenModel.inputMessage,
            onValueChange = { viewModel.processIntent(ChatScreenIntent.OnMessageInput(it)) },
            onSendClick = { viewModel.processIntent(ChatScreenIntent.OnMessageSendClick) }
        )
    }
}

@Composable
fun ChatBar(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    chatName: String,
    animationKey: String
) {
    with(sharedTransitionScope) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun ChatComponent(
    modifier: Modifier = Modifier,
    screenModel: ChatScreenModel
) {
    var keyboardHeight by remember { mutableFloatStateOf(0f) }
    var lastCurrentChatItem by remember { mutableIntStateOf(0) }
    val lazyScreenState = rememberLazyListState()
    val keyboardHeightCurrent = WindowInsets.ime.getBottom(LocalDensity.current).toFloat()

    LaunchedEffect(key1 = screenModel.chatItems) {
        if (screenModel.chatItems.lastOrNull()?.author == Author.ME) {
            lazyScreenState.animateScrollToItem(screenModel.chatItems.size)
        }
        if (lazyScreenState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == screenModel.chatItems.lastIndex) {
            lazyScreenState.scrollToItem(lazyScreenState.layoutInfo.totalItemsCount)
        }
        if(screenModel.chatItems.size - lastCurrentChatItem > 2){
            lazyScreenState.scrollToItem(screenModel.chatItems.size)
        }
        lastCurrentChatItem = screenModel.chatItems.size
    }

    LaunchedEffect(keyboardHeightCurrent) {
        val currentKeyboardSize = keyboardHeightCurrent - keyboardHeight
        if (lazyScreenState.canScrollForward) lazyScreenState.scrollBy(currentKeyboardSize)
        keyboardHeight = keyboardHeightCurrent
    }
    LazyColumn(
        state = lazyScreenState,
        modifier = modifier
    ) {

        items(items = screenModel.chatItems, key = { item -> item.id }) { item ->
            when (item.author) {
                Author.ME -> ChatUserMessage(message = item.message)
                Author.BOT -> ChatAiMessage(message = item.message)
                Author.SYSTEM -> Unit
            }
        }
        if (screenModel.botWrite) {
            item {
                ChatAiMessageAnimation()
            }
        }
    }
}