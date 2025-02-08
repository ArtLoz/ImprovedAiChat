package com.shadow.deepseekimp.ui.nav

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shadow.deepseekimp.ui.screens.chatselector.ScreenChatSelector
import com.shadow.deepseekimp.ui.screens.chat.HistoryChatScreen
import com.shadow.deepseekimp.ui.screens.chat.OneTimeChatScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navigationController: NavigationController,
) {
    SharedTransitionLayout {
        NavHost(
            modifier = modifier,
            navController = navigationController.navHostController,
            startDestination = NavScreens.SelectorChatScreen
        ) {
            composable<NavScreens.SelectorChatScreen> {
                ScreenChatSelector(
                    navigationController = navigationController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
            composable<NavScreens.HistoryChatScreen> {
                HistoryChatScreen(
                    navigationController = navigationController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }
            composable<NavScreens.OneTimeChatScreen> {
                OneTimeChatScreen(
                    navigationController = navigationController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}