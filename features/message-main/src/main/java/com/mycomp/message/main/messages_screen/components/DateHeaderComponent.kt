package com.mycomp.message.main.messages_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycomp.message.main.R
import com.mycomp.message.main.messages_screen.Constants.DATE_FORMAT_MONTH_DAY
import com.mycomp.message.main.messages_screen.Constants.DATE_FORMAT_YEAR
import com.mycomp.message.main.messages_screen.utils.isSameDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
internal fun DateHeader(date: Long) {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_MONTH_DAY, Locale.getDefault())
    val yearFormat = SimpleDateFormat(DATE_FORMAT_YEAR, Locale.getDefault())

    val messageDate = Calendar.getInstance().apply { timeInMillis = date }
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val formattedDate = dateFormat.format(messageDate.time)
    val formattedYear = yearFormat.format(messageDate.time)

    val displayText = when {
        messageDate.isSameDay(today) -> "Today, $formattedDate"
        messageDate.isSameDay(yesterday) -> "Yesterday, $formattedDate"
        messageDate.get(Calendar.YEAR) != today.get(Calendar.YEAR) -> "$formattedDate, $formattedYear"
        else -> formattedDate
    }

    val backgroundColor = if (isSystemInDarkTheme()) {
        Color(0xFF161727)
    } else {
        Color(0xFFA0A1B0)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = displayText,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            modifier = Modifier
                .align(Alignment.Center)
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 2.dp, horizontal = 12.dp)
        )
    }
}