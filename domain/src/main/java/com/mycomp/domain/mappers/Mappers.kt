package com.mycomp.domain.mappers

import com.mycomp.domain.models.MessageModel
import com.mycomp.message.data.models.Message

fun MessageModel.toMessage() = Message(
    id = this.id,
    text = this.text,
    photoUri = this.photoUri,
    videoUri = this.videoUri,
    date = this.date
)

fun Message.toDomain() = MessageModel(
    id = this.id,
    text = this.text ?: "",
    photoUri = this.photoUri ?: "",
    videoUri = this.videoUri ?: "",
    date = this.date
)