package com.mycomp.domain.usecases

import com.mycomp.message.data.MessagesRepository
import javax.inject.Inject

class ChangeMessageTextUseCase @Inject constructor(
    private val messageRepository: MessagesRepository
) {
    suspend operator fun invoke(id: Int, text: String) {
        messageRepository.changeMessageText(id, text)
    }
}