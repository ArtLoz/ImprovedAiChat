package com.shadow.deepseekimp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shadow.deepseekimp.data.database.model.ChatDbModel

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(chatDbModel: ChatDbModel)

    @Query("SELECT * FROM chat WHERE aiModel = :aiModel")
    suspend fun getAllChatMessages(aiModel:String): List<ChatDbModel>
}