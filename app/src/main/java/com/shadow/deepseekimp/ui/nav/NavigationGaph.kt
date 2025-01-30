package com.shadow.deepseekimp.ui.nav

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.shadow.deepseekimp.ui.screens.chatselector.ScreenChatSelector
import com.shadow.deepseekimp.ui.screens.chat.ChatScreen

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
            composable<NavScreens.ChatScreen> {
                val name = it.toRoute<NavScreens.ChatScreen>().chadTypeString
                ChatScreen(
                    navigationController = navigationController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    chatType = ChatType.valueOf(name)
                )
            }
        }
    }
}