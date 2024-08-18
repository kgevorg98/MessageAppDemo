package com.mycomp.message.main.messages_screen.utils

import android.content.Context
import android.net.Uri
import androidx.compose.ui.unit.Dp
import java.util.Calendar

internal fun Uri.isPhoto(context: Context): Boolean {
    val mimeType = context.contentResolver.getType(this)
    return mimeType?.startsWith("image") == true
}

internal fun Uri.isVideo(context: Context): Boolean {
    val mimeType = context.contentResolver.getType(this)
    return mimeType?.startsWith("video") == true
}

internal fun Calendar.isSameDay(other: Calendar): Boolean {
    return this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}

internal fun Dp.toPx(context: Context): Float {
    return this.value * context.resources.displayMetrics.density
}

