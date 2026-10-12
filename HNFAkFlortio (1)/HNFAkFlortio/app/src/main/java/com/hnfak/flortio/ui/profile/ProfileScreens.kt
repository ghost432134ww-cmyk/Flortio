package com.hnfak.flortio.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hnfak.flortio.R
import com.hnfak.flortio.domain.model.UserProfile
import com.hnfak.flortio.ui.common.AppViewModelProvider
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.messageRes
import com.hnfak.flortio.ui.components.BannerKind
import com.hnfak.flortio.ui.components.EmptyState
import com.hnfak.flortio.ui.components.FlortioCard
import com.hnfak.flortio.ui.components.FlortioTextField
import com.hnfak.flortio.ui.components.LoadingBlock
import com.hnfak.flortio.ui.components.PrimaryButton
import com.hnfak.flortio.ui.components.ScreenScaffold
import com.hnfak.flortio.ui.components.SecondaryButton
import com.hnfak.flortio.ui.components.StateColumn
import com.hnfak.flortio.ui.components.StatusBanner
import com.hnfak.flortio.ui.components.SubmitErrorBanner
import com.hnfak.flortio.ui.components.TabScreen
import com.hnfak.flortio.ui.theme.FlortioColors
import java.time.Year

// ---------------------------------------------------------------- Profil

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onLogin: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Düzenleme ekranından dönünce güncel profili yeniden yükle.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    TabScreen(
        title = stringResource(R.string.tab_profile),
        actions = {
            IconButton(onClick = onSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.profile_settings),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
    ) {
        when (val s = state) {
            is ProfileScreenState.Loading -> LoadingBlock()
            is ProfileScreenState.SignedOut -> StateColumn {
                EmptyState(
                    icon = Icons.Outlined.Person,
                    title = stringResource(R.string.profile_signed_out_title),
                    body = stringResource(R.string.profile_signed_out_body),
                    actionLabel = stringResource(R.string.welcome_login),
                    onAction = onLogin,
                )
            }
            is ProfileScreenState.Error -> StateColumn {
                StatusBanner(text = stringResource(s.error.messageRes()), kind = BannerKind.Error)
                Spacer(Modifier.height(24.dp))
                SecondaryButton(text = stringResource(R.string.retry), onClick = viewModel::refresh)
            }
            is ProfileScreenState.Loaded -> ProfileContent(
                email = s.user.email,
                profile = s.profile,
                onEditProfile = onEditProfile,
            )
        }
    }
}

@Composable
private fun ProfileContent(email: String, profile: UserProfile?, onEditProfile: () -> Unit) {
    val name = profile?.displayName?.takeIf { it.isNotBlank() }
    val hasContent = name != null
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FlortioCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Avatar()
                Spacer(Modifier.height(16.dp))
                Text(
                    text = name ?: stringResource(R.string.profile_name_missing),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (profile != null) {
                    ProfileFacts(profile)
                }
            }
        }
        if (!hasContent) {
            StatusBanner(
                text = stringResource(R.string.profile_incomplete_body),
                kind = BannerKind.Info,
            )
        }
        PrimaryButton(text = stringResource(R.string.profile_edit), onClick = onEditProfile)
    }
}

@Composable
private fun ProfileFacts(profile: UserProfile) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        profile.birthYear?.let { year ->
            val age = Year.now().value - year
            Fact(icon = Icons.Outlined.Cake, text = stringResource(R.string.profile_age_format, age))
        }
        if (profile.city.isNotBlank()) {
            Fact(icon = Icons.Outlined.LocationOn, text = profile.city)
        }
        if (profile.bio.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = profile.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun Fact(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Fotoğraf yüklemesi henüz yok; yer tutucu avatar. */
@Composable
private fun Avatar() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(FlortioColors.CardHigh)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(listOf(FlortioColors.Purple, FlortioColors.Lavender)),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = FlortioColors.Lavender,
            modifier = Modifier.size(44.dp),
        )
    }
}

// ----------------------------------------------------- Profil düzenleme

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.submit) {
        if (state.submit is SubmitState.Success) onSaved()
    }

    ScreenScaffold(title = stringResource(R.string.edit_title), onBack = onBack) {
        if (!state.backendConfigured) {
            StatusBanner(
                text = stringResource(R.string.edit_draft_note),
                kind = BannerKind.Info,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }
        SubmitErrorBanner(state.submit, Modifier.padding(bottom = 16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Avatar()
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.edit_photo_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FlortioTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = stringResource(R.string.edit_name_label),
                leadingIcon = Icons.Outlined.Person,
                errorText = state.nameError?.let { stringResource(it.messageRes()) },
            )
            FlortioTextField(
                value = state.birthYear,
                onValueChange = viewModel::onBirthYearChange,
                label = stringResource(R.string.edit_birth_label),
                leadingIcon = Icons.Outlined.Cake,
                keyboardType = KeyboardType.Number,
                errorText = state.birthYearError?.let { stringResource(it.messageRes()) },
            )
            FlortioTextField(
                value = state.city,
                onValueChange = viewModel::onCityChange,
                label = stringResource(R.string.edit_city_label),
                leadingIcon = Icons.Outlined.LocationOn,
                errorText = state.cityError?.let { stringResource(it.messageRes()) },
            )
            FlortioTextField(
                value = state.bio,
                onValueChange = viewModel::onBioChange,
                label = stringResource(R.string.edit_bio_label),
                singleLine = false,
                minLines = 4,
                imeAction = ImeAction.Default,
                supportingText = stringResource(R.string.edit_bio_counter, state.bio.length),
                errorText = state.bioError?.let { stringResource(it.messageRes()) },
            )
        }
        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = stringResource(R.string.edit_save),
            onClick = viewModel::save,
            loading = state.submit is SubmitState.Loading,
        )
        Spacer(Modifier.height(16.dp))
    }
}
