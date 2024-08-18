package com.mycomp.message.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.mycomp.database.MessagesDatabase
import com.mycomp.database.models.MessageEntity
import com.mycomp.message.data.mappers.toMessage
import com.mycomp.message.data.mappers.toMessageEntity
import com.mycomp.message.data.models.Message
import com.mycomp.message_core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val messagesDatabase: MessagesDatabase
) {
    private fun fetchMessages(
        pagingSourceFactory: () -> PagingSource<Int, MessageEntity>
    ): Flow<Result<PagingData<Message>>> {
        return flow {
            emit(Result.Loading)
            val pagingDataFlow = Pager(
                config = PagingConfig(
                    pageSize = 30,
                    prefetchDistance = 10,
                    enablePlaceholders = true
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow

            pagingDataFlow
                .map { pagingData ->
                    pagingData.map { messageEntity ->
                        messageEntity.toMessage()
                    }
                }
                .collect { pagingData ->
                    emit(Result.Success(pagingData))
                }
        }.catch { exception ->
            emit(Result.Error(exception))
        }
    }

    fun getAllMessages(): Flow<Result<PagingData<Message>>> {
        return fetchMessages { messagesDatabase.messagesDao.getMessages() }
    }

    fun searchMessagesByText(text: String): Flow<Result<PagingData<Message>>> {
        return fetchMessages { messagesDatabase.messagesDao.searchMessagesByName(text) }
    }

    suspend fun insertMessage(message: Message) {
        messagesDatabase.messagesDao.insertMessage(message.toMessageEntity())
    }

    suspend fun changeMessageText(messageId: Int, editedText: String): Result<Unit> {
        return try {
            val message = messagesDatabase.messagesDao.getMessageById(messageId)
            if (message != null) {
                val updatedProduct = message.copy(text = editedText)
                val rowsUpdated = messagesDatabase.messagesDao.updateMessage(updatedProduct)
                if (rowsUpdated > 0) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Update failed"))
                }
            } else {
                Result.Error(Exception("Message not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun removeMessageById(messageId: Int): Result<Unit> {
        return try {
            messagesDatabase.messagesDao.deleteMessageById(messageId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteAllMessages(): Result<Unit> {
        return try {
            messagesDatabase.messagesDao.deleteAllMessages()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
