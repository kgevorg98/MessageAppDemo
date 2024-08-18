package com.mycomp.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.mycomp.domain.mappers.toDomain
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.data.MessagesRepository
import com.mycomp.message_core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(): Flow<Result<PagingData<MessageModel>>> {
        return messagesRepository.getAllMessages()
            .map { result ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.map { it.toDomain() })
                    is Result.Error -> result
                    is Result.Loading -> result
                }
            }
    }
}