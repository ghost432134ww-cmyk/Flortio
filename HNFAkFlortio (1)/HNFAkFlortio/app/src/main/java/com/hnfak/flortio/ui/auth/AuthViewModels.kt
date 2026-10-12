package com.hnfak.flortio.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

// ---------------------------------------------------------------- Giriş

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
    val submit: SubmitState = SubmitState.Idle,
)

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update {
        it.copy(email = value, emailError = null, submit = it.submit.withoutFailure())
    }

    fun onPasswordChange(value: String) = _state.update {
        it.copy(password = value, passwordError = null, submit = it.submit.withoutFailure())
    }

    fun submit() {
        val current = _state.value
        if (current.submit is SubmitState.Loading) return

        val emailError = Validators.email(current.email)
        val passwordError = Validators.requiredPassword(current.password)
        if (emailError != null || passwordError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        _state.update { it.copy(submit = SubmitState.Loading) }
        viewModelScope.launch {
            val result = auth.signIn(current.email.trim(), current.password)
            _state.update { it.copy(submit = result.toSubmitState()) }
        }
    }
}

// ---------------------------------------------------------------- Kayıt

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val ageConfirmed: Boolean = false,
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
    val confirmationError: FieldError? = null,
    val ageError: FieldError? = null,
    val submit: SubmitState = SubmitState.Idle,
)

class RegisterViewModel(private val auth: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update {
        it.copy(email = value, emailError = null, submit = it.submit.withoutFailure())
    }

    fun onPasswordChange(value: String) = _state.update {
        it.copy(
            password = value,
            passwordError = null,
            confirmationError = null,
            submit = it.submit.withoutFailure(),
        )
    }

    fun onConfirmationChange(value: String) = _state.update {
        it.copy(confirmation = value, confirmationError = null, submit = it.submit.withoutFailure())
    }

    fun onAgeConfirmedChange(value: Boolean) = _state.update {
        it.copy(ageConfirmed = value, ageError = null, submit = it.submit.withoutFailure())
    }

    fun submit() {
        val current = _state.value
        if (current.submit is SubmitState.Loading) return

        val emailError = Validators.email(current.email)
        val passwordError = Validators.newPassword(current.password)
        val confirmationError = Validators.confirmPassword(current.password, current.confirmation)
        val ageError = Validators.mustBeChecked(current.ageConfirmed)
        if (listOf(emailError, passwordError, confirmationError, ageError).any { it != null }) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmationError = confirmationError,
                    ageError = ageError,
                )
            }
            return
        }

        _state.update { it.copy(submit = SubmitState.Loading) }
        viewModelScope.launch {
            val result = auth.signUp(current.email.trim(), current.password)
            _state.update { it.copy(submit = result.toSubmitState()) }
        }
    }
}

// ------------------------------------------------------- Şifre sıfırlama

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: FieldError? = null,
    val submit: SubmitState = SubmitState.Idle,
)

class ForgotPasswordViewModel(private val auth: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update {
        it.copy(email = value, emailError = null, submit = it.submit.withoutFailure())
    }

    fun submit() {
        val current = _state.value
        if (current.submit is SubmitState.Loading) return

        val emailError = Validators.email(current.email)
        if (emailError != null) {
            _state.update { it.copy(emailError = emailError) }
            return
        }

        _state.update { it.copy(submit = SubmitState.Loading) }
        viewModelScope.launch {
            val result = auth.sendPasswordReset(current.email.trim())
            _state.update { it.copy(submit = result.toSubmitState()) }
        }
    }
}
