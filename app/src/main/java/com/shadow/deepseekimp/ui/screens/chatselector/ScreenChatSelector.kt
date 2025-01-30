package com.shadow.deepseekimp.ui.screens.chatselector

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadow.deepseekimp.ui.nav.ChatType
import com.shadow.deepseekimp.ui.nav.NavScreens
import com.shadow.deepseekimp.ui.nav.NavigationController
import com.shadow.deepseekimp.R
import com.shadow.deepseekimp.ui.baseui.MainButton
import com.shadow.deepseekimp.ui.baseui.MainButtonOutlined
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_HISTORY
import com.shadow.deepseekimp.ui.utils.SHARED_TITLE_KEY_ONE

@Composable
fun ScreenChatSelector(
    modifier: Modifier = Modifier,
    navigationController: NavigationController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.ic_deepseek),
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.1f)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.chat_selector_choose_chat),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.fillMaxWidth(0.6f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_deepseek),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.chat_selector_by_deepseek),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                with(sharedTransitionScope) {
                    MainButton(
                        modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(SHARED_TITLE_KEY_ONE),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        buttonText = stringResource(R.string.chat_selector_one_chat)
                    ) {
                        navigationController.navigate(NavScreens.ChatScreen(ChatType.ONE_TIME.name, SHARED_TITLE_KEY_ONE))
                    }
                MainButtonOutlined(
                    modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(SHARED_TITLE_KEY_HISTORY),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                    buttonText = stringResource(R.string.chat_selector_history)
                ) {
                    navigationController.navigate(NavScreens.ChatScreen(ChatType.HISTORY.name, SHARED_TITLE_KEY_HISTORY))
                }
                }
            }
        }
    }
}