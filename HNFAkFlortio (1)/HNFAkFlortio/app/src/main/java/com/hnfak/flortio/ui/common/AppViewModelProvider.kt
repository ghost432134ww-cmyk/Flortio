package com.hnfak.flortio.ui.common

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hnfak.flortio.FlortioApp
import com.hnfak.flortio.di.AppContainer
import com.hnfak.flortio.ui.auth.ForgotPasswordViewModel
import com.hnfak.flortio.ui.auth.LoginViewModel
import com.hnfak.flortio.ui.auth.RegisterViewModel
import com.hnfak.flortio.ui.discover.BlockedUsersViewModel
import com.hnfak.flortio.ui.discover.DiscoverViewModel
import com.hnfak.flortio.ui.discover.MessagesViewModel
import com.hnfak.flortio.ui.profile.EditProfileViewModel
import com.hnfak.flortio.ui.profile.ProfileViewModel
import com.hnfak.flortio.ui.settings.DeleteAccountViewModel
import com.hnfak.flortio.ui.settings.SettingsViewModel

/** Tüm ViewModel'leri AppContainer'dan beslenen tek bir fabrika ile üretir. */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { LoginViewModel(container().authRepository) }
        initializer { RegisterViewModel(container().authRepository) }
        initializer { ForgotPasswordViewModel(container().authRepository) }
        initializer { DiscoverViewModel(container().discoverRepository) }
        initializer { MessagesViewModel(container().conversationRepository) }
        initializer { ProfileViewModel(container().authRepository, container().profileRepository) }
        initializer { EditProfileViewModel(container().authRepository, container().profileRepository) }
        initializer { SettingsViewModel(container().authRepository) }
        initializer { BlockedUsersViewModel(container().blockRepository) }
        initializer { DeleteAccountViewModel(container().authRepository) }
    }
}

private fun CreationExtras.container(): AppContainer {
    val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlortioApp
    return app.container
}
