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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                .padding(horizontal = 14.dp),
            screenModel = screenModel,
            onValueChange = { viewModel.processIntent(ChatScreenIntent.OnMessageInput(it)) },
            onSendClick = { viewModel.processIntent(ChatScreenIntent.OnMessageSendClick) },
            animationDone = {viewModel.processIntent(ChatScreenIntent.AnimationDoneMsg(it))}
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
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChatComponent(
    modifier: Modifier = Modifier,
    screenModel: ChatScreenModel,
    onValueChange :(String) -> Unit,
    onSendClick: () -> Unit,
    animationDone: (String) -> Unit
) {
    var keyboardHeight by remember { mutableFloatStateOf(0f) }
    var lastCurrentChatItem by remember { mutableIntStateOf(0) }
    var focusState by remember { mutableStateOf<FocusState?>(null) }
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
        if (lazyScreenState.canScrollForward && focusState == null) lazyScreenState.scrollBy(currentKeyboardSize)
        keyboardHeight = keyboardHeightCurrent
        if(keyboardHeightCurrent == 0f){
            focusState = null
        }
    }
    Column(modifier = modifier) {
        LazyColumn(
            state = lazyScreenState,
            modifier = Modifier.weight(1f)
        ) {

            items(items = screenModel.chatItems, key = { item -> item.id }) { item ->
                when (item.author) {
                    Author.ME -> ChatUserMessage(message = item.message)
                    Author.BOT -> ChatAiMessage(message = item.message, showAnimation = !item.alreadyAnimated){
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
        }
        ChatInput(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focus ->
                    if(!focus.isFocused){
                        focusState = focus
                    }
                },
            value = screenModel.inputMessage,
            onValueChange = onValueChange,
            onSendClick = onSendClick,
            lock = screenModel.botWrite
        )
    }

}