package com.mycomp.message.main.messages_screen

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.main.messages_screen.components.BottomInputBar
import com.mycomp.message.main.messages_screen.components.MessageListScreen
import com.mycomp.message.main.messages_screen.components.TopBar

@Composable
fun MessagesScreen() {
    val viewModel: MessageViewModel = viewModel()
    var editingMessage by remember { mutableStateOf<MessageModel?>(null) }
    var longPressedMessageId by remember { mutableStateOf<Int?>(null) }

    val listState = rememberLazyListState()
    val messages = viewModel.messages.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBar(
                onSearch = { viewModel.setSearchQuery(it) },
                hasMessages = messages.itemCount > 0,
                onDeleteAllMessages = { viewModel.deleteAllMessages() }
            )
        },
        bottomBar = {
            BottomInputBar(
                onSendMessage = { message ->
                    if (editingMessage != null) {
                        viewModel.editMessage(message)
                        editingMessage = null
                    } else {
                        viewModel.insertMessage(message)
                    }
                    longPressedMessageId = null
                },
                editingMessage = editingMessage,
                onEditingDone = {
                    editingMessage = null
                    longPressedMessageId = null
                }
            )
        }
    ) { paddingValues ->
        MessageListScreen(
            messages = messages,
            onDeleteMessage = { messageId ->
                viewModel.removeSingleMessageById(messageId)
                longPressedMessageId = null
            },
            onEditMessage = { message ->
                editingMessage = message
                longPressedMessageId = message.id
            },
            longPressedMessageId = longPressedMessageId,
            onLongPressMessage = { messageId ->
                longPressedMessageId = messageId
            },
            listState = listState,
            paddingValues = paddingValues
        )
    }
}






