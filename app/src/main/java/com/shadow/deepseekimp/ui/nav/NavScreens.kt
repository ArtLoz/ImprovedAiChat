package com.shadow.deepseekimp.ui.nav

import com.shadow.deepseekimp.R
import kotlinx.serialization.Serializable

sealed class NavScreens {

    @Serializable
    data object SelectorChatScreen : NavScreens()

    @Serializable
    data class ChatScreen(
        val chadTypeString: String,
    ) : NavScreens()
}

@Serializable
enum class ChatType(val value:Int){
    ONE_TIME(R.string.chat_selector_one_chat),
    HISTORY(R.string.chat_selector_history)
}