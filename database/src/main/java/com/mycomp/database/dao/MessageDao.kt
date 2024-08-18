package com.mycomp.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mycomp.database.models.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY date DESC")
    fun getMessages(): PagingSource<Int, MessageEntity>

    @Query("SELECT * FROM messages WHERE text LIKE '%' || :text || '%' ORDER BY date DESC")
    fun searchMessagesByName(text: String): PagingSource<Int, MessageEntity>

    @Update
    suspend fun updateMessage(message: MessageEntity): Int

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: Int): MessageEntity?

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: Int)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

}