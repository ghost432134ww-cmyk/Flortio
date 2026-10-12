package com.hnfak.flortio.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnfak.flortio.domain.AppError
import com.hnfak.flortio.domain.DataResult
import com.hnfak.flortio.domain.model.AuthUser
import com.hnfak.flortio.domain.model.UserProfile
import com.hnfak.flortio.domain.repository.AuthRepository
import com.hnfak.flortio.domain.repository.ProfileRepository
import com.hnfak.flortio.domain.validation.FieldError
import com.hnfak.flortio.domain.validation.Validators
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.toSubmitState
import com.hnfak.flortio.ui.common.withoutFailure
import java.time.Year
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ---------------------------------------------------------------- Profil

sealed interface ProfileScreenState {
    data object Loading : ProfileScreenState
    data object SignedOut : ProfileScreenState
    data class Loaded(val user: AuthUser, val profile: UserProfile?) : ProfileScreenState
    data class Error(val error: AppError) : ProfileScreenState
}

class ProfileViewModel(
    private val auth: AuthRepository,
    private val profiles: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    private var job: Job? = null

    /** Ekran her göründüğünde çağrılır; düzenleme sonrası güncel veriyi getirir. */
    fun refresh() {
        job?.cancel()
        job = viewModelScope.launch {
            val user = auth.currentUser.value
            if (user == null) {
                _state.value = ProfileScreenState.SignedOut
                return@launch
            }
            if (_state.value !is ProfileScreenState.Loaded) {
                _state.value = ProfileScreenState.Loading
            }
            _state.value = when (val result = profiles.loadProfile(user.id)) {
                is DataResult.Success -> ProfileScreenState.Loaded(user, result.value)
                is DataResult.Failure -> ProfileScreenState.Error(result.error)
            }
        }
    }
}

// ----------------------------------------------------- Profil düzenleme

data class EditProfileUiState(
    val name: String = "",
    val birthYear: String = "",
    val city: String = "",
    val bio: String = "",
    val nameError: FieldError? = null,
    val birthYearError: FieldError? = null,
    val cityError: FieldError? = null,
    val bioError: FieldError? = null,
    val backendConfigured: Boolean = false,
    val submit: SubmitState = SubmitState.Idle,
)

class EditProfileViewModel(
    private val auth: AuthRepository,
    private val profiles: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileUiState(backendConfigured = auth.isBackendConfigured))
    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()

    init {
        val user = auth.currentUser.value
        if (user != null) {
            viewModelScope.launch {
                val result = profiles.loadProfile(user.id)
                if (result is DataResult.Success && result.value != null) {
                    val profile = result.value
                    _state.update {
                        it.copy(
                            name = profile.displayName,
                            birthYear = profile.birthYear?.toString().orEmpty(),
                            city = profile.city,
                            bio = profile.bio,
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(value: String) = _state.update {
        it.copy(name = value, nameError = null, submit = it.submit.withoutFailure())
    }

    fun onBirthYearChange(value: String) = _state.update {
        it.copy(
            birthYear = value.filter(Char::isDigit).take(4),
            birthYearError = null,
            submit = it.submit.withoutFailure(),
        )
    }

    fun onCityChange(value: String) = _state.update {
        it.copy(city = value, cityError = null, submit = it.submit.withoutFailure())
    }

    fun onBioChange(value: String) = _state.update {
        it.copy(bio = value, bioError = null, submit = it.submit.withoutFailure())
    }

    fun save() {
        val current = _state.value
        if (current.submit is SubmitState.Loading) return

        val nameError = Validators.displayName(current.name)
        val birthYearError = Validators.birthYear(current.birthYear, Year.now().value)
        val cityError = Validators.city(current.city)
        val bioError = Validators.bio(current.bio)
        if (listOf(nameError, birthYearError, cityError, bioError).any { it != null }) {
            _state.update {
                it.copy(
                    nameError = nameError,
                    birthYearError = birthYearError,
                    cityError = cityError,
                    bioError = bioError,
                )
            }
            return
        }

        val user = auth.currentUser.value
        if (user == null) {
            _state.update { it.copy(submit = SubmitState.Failed(AppError.NotSignedIn)) }
            return
        }

        _state.update { it.copy(submit = SubmitState.Loading) }
        viewModelScope.launch {
            val profile = UserProfile(
                displayName = current.name.trim(),
                bio = current.bio.trim(),
                city = current.city.trim(),
                birthYear = current.birthYear.trim().toIntOrNull(),
            )
            val result = profiles.saveProfile(user.id, profile)
            _state.update { it.copy(submit = result.toSubmitState()) }
        }
    }
}
