package com.mycomp.domain.usecases

import com.mycomp.message.data.MessagesRepository
import javax.inject.Inject

class DeleteAllMessagesUseCase @Inject constructor(
    private val messageRepository: MessagesRepository
) {
    suspend operator fun invoke() {
        messageRepository.deleteAllMessages()
    }
}