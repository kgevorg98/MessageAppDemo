package com.mycomp.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String? = null,
    val photoUri: String? = null,
    val videoUri: String? = null,
    val date: Date = Date()
)