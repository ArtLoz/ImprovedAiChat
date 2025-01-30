package com.shadow.deepseekimp.domain.model.chat

enum class Author(val value: String) {
    ME("user"),
    BOT("assistant"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String): Author? {
            return entries.firstOrNull { it.value == value }
        }
    }
}