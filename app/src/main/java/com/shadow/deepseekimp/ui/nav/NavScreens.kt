package com.shadow.deepseekimp.ui.nav

import kotlinx.serialization.Serializable

sealed class NavScreens {

    @Serializable
    data object SelectorChatScreen : NavScreens()

    @Serializable
    data object HistoryChatScreen : NavScreens()
    @Serializable
    data object OneTimeChatScreen : NavScreens()
}
