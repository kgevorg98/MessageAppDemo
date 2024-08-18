package com.mycomp.message.main.messages_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mycomp.domain.models.MessageModel
import com.mycomp.domain.usecases.ChangeMessageTextUseCase
import com.mycomp.domain.usecases.DeleteAllMessagesUseCase
import com.mycomp.domain.usecases.GetAllMessagesUseCase
import com.mycomp.domain.usecases.InsertMessageUseCase
import com.mycomp.domain.usecases.DeleteMessageUseCase
import com.mycomp.domain.usecases.SearchMessagesByTextUseCase
import com.mycomp.message_core.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val searchMessagesByTextUseCase: SearchMessagesByTextUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val changeMessageTextUseCase: ChangeMessageTextUseCase,
    private val deleteAllMessagesUseCase: DeleteAllMessagesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: Flow<PagingData<MessageModel>> = searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            getAllMessagesUseCase().map { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> PagingData.empty()
                    is Result.Loading -> PagingData.empty()
                }
            }
        } else {
            searchMessagesByTextUseCase(query).map { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> PagingData.empty()
                    is Result.Loading -> PagingData.empty()
                }
            }
        }
    }.cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PagingData.empty()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insertMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            insertMessageUseCase(messageModel)
        }
    }

    fun editMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            changeMessageTextUseCase(messageModel.id, messageModel.text)
        }
    }

    fun removeSingleMessageById(messageId: Int) {
        viewModelScope.launch {
            deleteMessageUseCase(messageId)
        }
    }

    fun deleteAllMessages() {
        viewModelScope.launch {
            deleteAllMessagesUseCase()
        }
    }
}