package com.hnfak.flortio.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnfak.flortio.domain.model.AuthUser
import com.hnfak.flortio.domain.repository.AuthRepository
import com.hnfak.flortio.domain.validation.FieldError
import com.hnfak.flortio.domain.validation.Validators
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.toSubmitState
import com.hnfak.flortio.ui.common.withoutFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val auth: AuthRepository) : ViewModel() {
    val user: StateFlow<AuthUser?> = auth.currentUser
    val backendConfigured: Boolean = auth.isBackendConfigured

    private val _logout = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val logout: StateFlow<SubmitState> = _logout.asStateFlow()

    fun signOut() {
        if (_logout.value is SubmitState.Loading) return
        _logout.value = SubmitState.Loading
        viewModelScope.launch { _logout.value = auth.signOut().toSubmitState() }
    }
}

data class DeleteAccountUiState(
    val password: String = "",
    val confirmed: Boolean = false,
    val passwordError: FieldError? = null,
    val confirmError: FieldError? = null,
    val submit: SubmitState = SubmitState.Idle,
)

class DeleteAccountViewModel(private val auth: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(DeleteAccountUiState())
    val state: StateFlow<DeleteAccountUiState> = _state.asStateFlow()

    fun onPasswordChange(value: String) = _state.update {
        it.copy(password = value, passwordError = null, submit = it.submit.withoutFailure())
    }

    fun onConfirmedChange(value: Boolean) = _state.update {
        it.copy(confirmed = value, confirmError = null, submit = it.submit.withoutFailure())
    }

    fun submit() {
        val current = _state.value
        if (current.submit is SubmitState.Loading) return

        val passwordError = Validators.requiredPassword(current.password)
        val confirmError = Validators.mustBeChecked(current.confirmed)
        if (passwordError != null || confirmError != null) {
            _state.update { it.copy(passwordError = passwordError, confirmError = confirmError) }
            return
        }

        _state.update { it.copy(submit = SubmitState.Loading) }
        viewModelScope.launch {
            val result = auth.deleteAccount(current.password)
            _state.update { it.copy(submit = result.toSubmitState()) }
        }
    }
}
