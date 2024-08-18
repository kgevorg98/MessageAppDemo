package com.mycomp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mycomp.database.dao.MessageDao
import com.mycomp.database.models.MessageEntity
import com.mycomp.database.utils.Converters

class MessagesDatabase internal constructor(private val database: MessageRoomDatabase) {
    val messagesDao: MessageDao
        get() = database.messagesDao()
}

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class MessageRoomDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessageDao
}

fun MessagesDatabase(applicationContext: Context): MessagesDatabase {
    val newsRoomDatabase =
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            MessageRoomDatabase::class.java,
            "messages_database"
        ).build()
    return MessagesDatabase(newsRoomDatabase)
}