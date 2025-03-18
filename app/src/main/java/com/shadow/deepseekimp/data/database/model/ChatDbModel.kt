package com.shadow.deepseekimp.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val role: String,
    val message: String,
    val timestamp: Long,
    val aiModel: String,

) {
    enum class Role() {
        ASSISTANT, USER, SYSTEM
    }

    enum class AiModelDb {
        QWEEN, DEEPSEEK, QWEN_PLUS
    }
}
