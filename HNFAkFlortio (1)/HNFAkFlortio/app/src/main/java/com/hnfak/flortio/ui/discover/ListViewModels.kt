package com.hnfak.flortio.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnfak.flortio.domain.DataResult
import com.hnfak.flortio.domain.model.BlockedUser
import com.hnfak.flortio.domain.model.Conversation
import com.hnfak.flortio.domain.model.DiscoverProfile
import com.hnfak.flortio.domain.repository.BlockRepository
import com.hnfak.flortio.domain.repository.ConversationRepository
import com.hnfak.flortio.domain.repository.DiscoverRepository
import com.hnfak.flortio.ui.common.ListState
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.toListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(private val repository: DiscoverRepository) : ViewModel() {
    private val _state = MutableStateFlow<ListState<DiscoverProfile>>(ListState.Loading)
    val state: StateFlow<ListState<DiscoverProfile>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = ListState.Loading
        viewModelScope.launch { _state.value = repository.loadCandidates().toListState() }
    }
}

class MessagesViewModel(private val repository: ConversationRepository) : ViewModel() {
    private val _state = MutableStateFlow<ListState<Conversation>>(ListState.Loading)
    val state: StateFlow<ListState<Conversation>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = ListState.Loading
        viewModelScope.launch { _state.value = repository.loadConversations().toListState() }
    }
}

class BlockedUsersViewModel(private val repository: BlockRepository) : ViewModel() {
    private val _state = MutableStateFlow<ListState<BlockedUser>>(ListState.Loading)
    val state: StateFlow<ListState<BlockedUser>> = _state.asStateFlow()

    /** Engeli kaldırma sırasında oluşan hata (liste durumundan bağımsız). */
    private val _unblock = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val unblock: StateFlow<SubmitState> = _unblock.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = ListState.Loading
        viewModelScope.launch { _state.value = repository.loadBlockedUsers().toListState() }
    }

    fun unblock(userId: String) {
        _unblock.value = SubmitState.Loading
        viewModelScope.launch {
            when (val result = repository.unblock(userId)) {
                is DataResult.Success -> {
                    _unblock.value = SubmitState.Idle
                    load()
                }
                is DataResult.Failure -> _unblock.value = SubmitState.Failed(result.error)
            }
        }
    }
}
