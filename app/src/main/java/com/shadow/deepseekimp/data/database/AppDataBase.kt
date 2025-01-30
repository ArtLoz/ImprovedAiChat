package com.shadow.deepseekimp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shadow.deepseekimp.data.database.dao.ChatDao
import com.shadow.deepseekimp.data.database.model.ChatDbModel

@Database(entities = [ChatDbModel::class], version = AppDataBase.DATABASE_VERSION, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    abstract fun chatMessageDao(): ChatDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "chat_db"
    }
}