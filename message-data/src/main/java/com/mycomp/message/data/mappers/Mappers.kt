package com.mycomp.message.data.mappers

import com.mycomp.database.models.MessageEntity
import com.mycomp.message.data.models.Message

fun MessageEntity.toMessage() = Message(
    id = id,
    text = text,
    date = date,
    photoUri = photoUri,
    videoUri = videoUri
)

fun Message.toMessageEntity() = MessageEntity(
    id = id,
    text = text,
    date = date,
    photoUri = photoUri,
    videoUri = videoUri
)