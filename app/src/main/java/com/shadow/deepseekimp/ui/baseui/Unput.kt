package com.shadow.deepseekimp.ui.baseui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.R

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    value: String,
    lock: Boolean,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = !lock,
        placeholder = {
            Text(
                text = stringResource(R.string.chat_screen_hint)
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            if (lock) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                focusManager.clearFocus()
                                onSendClick.invoke()
                            },
                            interactionSource = null,
                            indication = null
                        ),
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.cd_button_send_msg)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                focusManager.clearFocus()
                onSendClick.invoke()
            }
        )
    )
}