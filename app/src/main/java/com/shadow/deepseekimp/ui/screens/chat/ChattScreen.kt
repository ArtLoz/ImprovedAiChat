package com.shadow.deepseekimp.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shadow.deepseekimp.domain.chat.ChatItemModel
import com.shadow.deepseekimp.ui.baseui.ChatAiMessage
import com.shadow.deepseekimp.ui.baseui.ChatAiMessageAnimation
import com.shadow.deepseekimp.ui.baseui.ChatInput
import com.shadow.deepseekimp.ui.baseui.ChatUserMessage
import com.shadow.deepseekimp.ui.screens.chat.model.ChatScreenIntent

@OptIn(ExperimentalLayoutApi::class)
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
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .weight(1f)
        ) {
            itemsIndexed(
                items = screenModel.chatItems,
                key = { _, item -> item.id }
            ) { _, item ->
                when (item.author) {
                    ChatItemModel.Author.ME -> ChatUserMessage(message = item.message)
                    ChatItemModel.Author.BOT -> ChatAiMessage(message = item.message) { }
                    ChatItemModel.Author.SYSTEM -> Unit
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