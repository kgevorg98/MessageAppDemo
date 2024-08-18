package com.mycomp.domain.usecases

import com.mycomp.domain.mappers.toMessage
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.data.MessagesRepository
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(message: MessageModel) {
        messagesRepository.insertMessage(message.toMessage())
    }
}