package com.shadow.deepseekimp.ui.baseui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.ui.theme.colorDeepSeek

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = buttonText
        )
    }
}

@Composable
fun MainButtonOutlined(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors().copy(
            contentColor = colorDeepSeek
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = buttonText
        )
    }
}