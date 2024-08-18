package com.mycomp.domain.models

import java.util.Date

data class MessageModel(
    val id: Int = 0,
    val text: String = "",
    val photoUri: String = "",
    val videoUri: String = "",
    val date: Date = Date()
)