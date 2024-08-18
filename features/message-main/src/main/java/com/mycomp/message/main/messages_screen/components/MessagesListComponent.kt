package com.mycomp.message.main.messages_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.main.messages_screen.Constants.DATE_FORMAT_YEAR_MONTH_DAY
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun MessageListScreen(
    messages: LazyPagingItems<MessageModel>,
    onDeleteMessage: (Int) -> Unit,
    onEditMessage: (MessageModel) -> Unit,
    longPressedMessageId: Int?,
    onLongPressMessage: (Int) -> Unit,
    listState: LazyListState,
    paddingValues: PaddingValues
) {

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.ime)
            .imePadding()
    ) {
        val groupedMessages = messages.itemSnapshotList.items.groupBy {
            SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY, Locale.getDefault()).format(it.date)
        }

        groupedMessages.forEach { (date, messagesForDate) ->
            items(
                count = messagesForDate.size,
                key = { messagesForDate[it].id },
                contentType = { "contentType" }
            ) { index ->
                MessageItem(
                    message = messagesForDate[index],
                    isLongPressed = longPressedMessageId == messagesForDate[index].id,
                    onDeleteMessage = onDeleteMessage,
                    onEditMessage = onEditMessage,
                    onLongPressMessage = onLongPressMessage
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SimpleDateFormat(
                    DATE_FORMAT_YEAR_MONTH_DAY,
                    Locale.getDefault()
                ).parse(date)?.let {
                    DateHeader(
                        date = it.time
                    )
                }
            }
        }
    }

    LaunchedEffect(messages.itemSnapshotList.items.size) {
        listState.animateScrollToItem(0)
    }
}
