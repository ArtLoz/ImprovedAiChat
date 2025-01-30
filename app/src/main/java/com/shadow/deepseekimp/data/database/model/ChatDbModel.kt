package com.shadow.deepseekimp.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val role: Int,
    val message: String,
    val timestamp: Long
    ) {
    enum class Role(val type: Int, val value: String) {
        ASSISTANT(0, "assistant"), USER(1, "user")
    }
}
