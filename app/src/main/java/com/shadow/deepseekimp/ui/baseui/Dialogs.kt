package com.shadow.deepseekimp.ui.baseui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.R


@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    dialogText: String,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card(
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 18.dp)
                    .align(Alignment.CenterHorizontally),
                text = dialogText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 18.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(44.dp)
            ) {
                MainButtonOutlined(
                    modifier = Modifier.weight(1F),
                    buttonText = stringResource(R.string.dialog_cancel),
                    onClick = onDismissRequest
                )
                MainButton(
                    modifier = Modifier.weight(1F),
                    buttonText = stringResource(R.string.dialog_ok),
                    onClick = {
                        onConfirm()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}