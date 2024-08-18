package com.mycomp.message.data.models

import java.util.Date

data class Message (
   val id: Int = 0,
    val text: String? = null,
    val photoUri: String? = null,
    val videoUri: String? = null,
    val date: Date = Date()
)