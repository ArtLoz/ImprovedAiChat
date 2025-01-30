package com.shadow.deepseekimp.ui.baseui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.R
import kotlinx.coroutines.delay

@Composable
fun ChatAiMessage(
    modifier: Modifier = Modifier,
    message: String,
    onClickCopy: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.8f)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_deepseek),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
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
                            onClickCopy()
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
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_deepseek),
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
        modifier = Modifier
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