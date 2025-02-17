package com.shadow.deepseekimp.ui.baseui

import android.content.ClipboardManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownPadding
import com.shadow.deepseekimp.R
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import kotlinx.coroutines.delay

@Composable
fun ChatAiMessage(
    modifier: Modifier = Modifier,
    message: String,
    showAnimation: Boolean = false,
    animationDone: () ->Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(isDarkTheme) {
        Highlights.Builder()
            .theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }
    val clipboardManager = LocalClipboardManager.current
    var displayedMessage by remember { mutableStateOf("") }
    LaunchedEffect(message) {
        displayedMessage = ""
        if (showAnimation) {
            message.forEach { char ->
                displayedMessage += char
                delay(5)
            }
            animationDone()
        } else {
            displayedMessage = message
        }
    }
    Column(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.9f)
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_logo_svg),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Markdown(
                content = displayedMessage,
                components = markdownComponents(
                    codeBlock = { MarkdownHighlightedCodeBlock(it.content, it.node, highlightsBuilder) },
                    codeFence = { MarkdownHighlightedCodeFence(it.content, it.node, highlightsBuilder) },
                ),
                typography = markdownTypography(
                    h3 = MaterialTheme.typography.titleMedium,
                    text = MaterialTheme.typography.bodyMedium,
                    paragraph = MaterialTheme.typography.bodyMedium,
                    code = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp),
                ),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(message))
                        },
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ChatAiMessageAnimation(
    modifier: Modifier = Modifier,
) {
    var chatDotAnimation by remember {
        mutableStateOf(".")
    }
    LaunchedEffect(Unit) {
        while (true) {
            chatDotAnimation += "."
            if (chatDotAnimation.length > 4) {
                chatDotAnimation = "."
            }
            delay(500)
        }
    }
    Row(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_logo_svg),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = chatDotAnimation,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ChatUserMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .sizeIn(minWidth = 0.dp, maxWidth = (screenWidth * 0.85).dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 10.dp
                    )
                )
                .padding(8.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}