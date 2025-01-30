package com.shadow.deepseekimp.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.shadow.deepseekimp.domain.chat.ChatItemModel
import com.shadow.deepseekimp.ui.baseui.ChatAiMessage
import com.shadow.deepseekimp.ui.baseui.ChatAiMessageAnimation
import com.shadow.deepseekimp.ui.baseui.ChatInput
import com.shadow.deepseekimp.ui.baseui.ChatUserMessage
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatScreenViewModel = hiltViewModel()
) {
    val screenModel = viewModel.screenModel.collectAsState().value
    Column(
        modifier = modifier
            .imePadding()
            .fillMaxHeight()
    ) {
        ChatBar(chatName = "Chat")
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
    chatName: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = chatName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ChatComponent(
    modifier: Modifier = Modifier,
    screenModel: ChatScreenModel
) {
    var keyboardHeight by remember { mutableFloatStateOf(0f) }
    val lazyScreenState = rememberLazyListState()
    val keyboardHeightCurrent = WindowInsets.ime.getBottom(LocalDensity.current).toFloat()

    LaunchedEffect(key1 =screenModel.chatItems) {
        if (screenModel.chatItems.lastOrNull()?.author == ChatItemModel.Author.ME) {
            lazyScreenState.animateScrollToItem(screenModel.chatItems.size)
        }
        if (lazyScreenState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == screenModel.chatItems.lastIndex) {
            lazyScreenState.scrollToItem(lazyScreenState.layoutInfo.totalItemsCount)
        }
    }

    LaunchedEffect(keyboardHeightCurrent) {
            val currentKeyboardSize = keyboardHeightCurrent - keyboardHeight
            if(lazyScreenState.canScrollForward) lazyScreenState.scrollBy(currentKeyboardSize)
            keyboardHeight = keyboardHeightCurrent
    }
    LazyColumn(
        state = lazyScreenState,
        modifier = modifier
    ) {

        items(items = screenModel.chatItems, key = { item -> item.id}){ item ->
            when (item.author) {
                ChatItemModel.Author.ME -> ChatUserMessage(message = item.message)
                ChatItemModel.Author.BOT -> ChatAiMessage(message = item.message)
                ChatItemModel.Author.SYSTEM -> Unit
            }
        }
        if (screenModel.botWrite) {
            item {
                ChatAiMessageAnimation()
            }
        }
    }
}