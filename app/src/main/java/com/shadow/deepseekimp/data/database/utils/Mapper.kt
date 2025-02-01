package com.shadow.deepseekimp.data.database.utils

import com.shadow.deepseekimp.data.database.model.ChatDbModel
import com.shadow.deepseekimp.domain.model.chat.AiModel
import com.shadow.deepseekimp.domain.model.chat.Author
import com.shadow.deepseekimp.domain.model.chat.ChatItemModel

fun ChatItemModel.toDbModel(): ChatDbModel{
    return ChatDbModel(
        role = this.author.toDbRole().name,
        message = this.message,
        timestamp = this.time,
        aiModel = this.aiModel.toAiDbModel().name
    )
}

fun Author.toDbRole(): ChatDbModel.Role{
    return when(this){
        Author.ME -> ChatDbModel.Role.USER
        Author.BOT -> ChatDbModel.Role.ASSISTANT
        Author.SYSTEM -> ChatDbModel.Role.SYSTEM
    }
}

fun ChatDbModel.Role.toAuthor(): Author{
    return when(this){
        ChatDbModel.Role.USER -> Author.ME
        ChatDbModel.Role.ASSISTANT -> Author.BOT
        ChatDbModel.Role.SYSTEM -> Author.SYSTEM
    }
}

fun AiModel.toAiDbModel(): ChatDbModel.AiModelDb{
    return when(this){
        AiModel.DEEEP_SEEK -> ChatDbModel.AiModelDb.DEEPSEEK
        AiModel.QWEN_MAX -> ChatDbModel.AiModelDb.QWEEN
    }
}

fun ChatDbModel.AiModelDb.toAiModel(): AiModel{
    return when(this){
        ChatDbModel.AiModelDb.DEEPSEEK -> AiModel.DEEEP_SEEK
        ChatDbModel.AiModelDb.QWEEN -> AiModel.QWEN_MAX
    }
}

fun ChatDbModel.toChatItemModel(): ChatItemModel{
    return ChatItemModel(
        message = this.message,
        author = ChatDbModel.Role.valueOf(this.role).toAuthor(),
        time = this.timestamp,
        aiModel = ChatDbModel.AiModelDb.valueOf(this.aiModel).toAiModel(),
        alreadyAnimated = true
    )
}

