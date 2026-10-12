package com.hnfak.flortio.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hnfak.flortio.BuildConfig
import com.hnfak.flortio.R
import com.hnfak.flortio.ui.common.AppViewModelProvider
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.messageRes
import com.hnfak.flortio.ui.discover.BlockedUsersViewModel
import com.hnfak.flortio.ui.components.BannerKind
import com.hnfak.flortio.ui.components.CardDivider
import com.hnfak.flortio.ui.components.FlortioCard
import com.hnfak.flortio.ui.components.FlortioTextField
import com.hnfak.flortio.ui.components.ListStateContent
import com.hnfak.flortio.ui.components.PrimaryButton
import com.hnfak.flortio.ui.components.ScreenScaffold
import com.hnfak.flortio.ui.components.SectionLabel
import com.hnfak.flortio.ui.components.SettingsRow
import com.hnfak.flortio.ui.components.SoonBadge
import com.hnfak.flortio.ui.components.StatusBanner
import com.hnfak.flortio.ui.components.SubmitErrorBanner
import com.hnfak.flortio.ui.auth.CheckboxRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.input.ImeAction

// --------------------------------------------------------------- Ayarlar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onPrivacy: () -> Unit,
    onBlocked: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val logout by viewModel.logout.collectAsStateWithLifecycle()
    LaunchedEffect(logout) {
        if (logout is SubmitState.Success) onLoggedOut()
    }
    val signedIn = user != null

    ScreenScaffold(title = stringResource(R.string.settings_title), onBack = onBack) {
        SubmitErrorBanner(logout, Modifier.padding(top = 8.dp))

        SectionLabel(stringResource(R.string.settings_section_account))
        FlortioCard {
            SettingsRow(
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.settings_privacy),
                subtitle = stringResource(R.string.settings_privacy_sub),
                onClick = onPrivacy,
            )
            CardDivider()
            SettingsRow(
                icon = Icons.Outlined.Block,
                title = stringResource(R.string.settings_blocked),
                subtitle = stringResource(R.string.settings_blocked_sub),
                onClick = onBlocked,
            )
            CardDivider()
            SettingsRow(
                icon = Icons.Outlined.DeleteForever,
                title = stringResource(R.string.settings_delete),
                subtitle = stringResource(R.string.settings_delete_sub),
                onClick = onDeleteAccount,
                tint = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(16.dp))
        FlortioCard {
            SettingsRow(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = stringResource(R.string.settings_logout),
                subtitle = if (signedIn) null else stringResource(R.string.settings_logout_sub_signed_out),
                onClick = if (signedIn) viewModel::signOut else null,
                tint = if (signedIn) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }

        SectionLabel(stringResource(R.string.settings_section_app))
        FlortioCard {
            SettingsRow(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.settings_version),
                trailing = {
                    Text(
                        text = BuildConfig.VERSION_NAME,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
            CardDivider()
            SettingsRow(
                icon = if (viewModel.backendConfigured) Icons.Outlined.Cloud else Icons.Outlined.CloudOff,
                title = stringResource(R.string.settings_backend),
                trailing = {
                    Text(
                        text = stringResource(
                            if (viewModel.backendConfigured) {
                                R.string.settings_backend_ready
                            } else {
                                R.string.settings_backend_missing
                            },
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

// --------------------------------------------------------------- Gizlilik

@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    ScreenScaffold(title = stringResource(R.string.privacy_title), onBack = onBack) {
        StatusBanner(
            text = stringResource(R.string.privacy_banner),
            kind = BannerKind.Info,
            modifier = Modifier.padding(top = 8.dp),
        )

        SectionLabel(stringResource(R.string.privacy_data_title))
        FlortioCard {
            Text(
                text = stringResource(R.string.privacy_data_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp),
            )
        }

        SectionLabel(stringResource(R.string.privacy_planned_section))
        FlortioCard {
            PlannedRow(Icons.Outlined.Lock, stringResource(R.string.privacy_visibility))
            CardDivider()
            PlannedRow(Icons.Outlined.Info, stringResource(R.string.privacy_location))
            CardDivider()
            PlannedRow(Icons.Outlined.Info, stringResource(R.string.privacy_online))
            CardDivider()
            PlannedRow(Icons.Outlined.Info, stringResource(R.string.privacy_export))
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** Henüz çalışmayan tercih: tıklanamaz ve "Yakında" etiketiyle açıkça işaretli. */
@Composable
private fun PlannedRow(icon: ImageVector, title: String) {
    SettingsRow(
        icon = icon,
        title = title,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        trailing = { SoonBadge() },
    )
}

// ------------------------------------------------------------ Engellenenler

@Composable
fun BlockedUsersScreen(
    onBack: () -> Unit,
    viewModel: BlockedUsersViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val unblock by viewModel.unblock.collectAsStateWithLifecycle()

    ScreenScaffold(title = stringResource(R.string.blocked_title), onBack = onBack, scrollable = false) {
        SubmitErrorBanner(unblock, Modifier.padding(bottom = 12.dp))
        Box(modifier = Modifier.weight(1f)) {
            ListStateContent(
                state = state,
                emptyIcon = Icons.Outlined.Block,
                emptyTitle = stringResource(R.string.blocked_empty_title),
                emptyBody = stringResource(R.string.blocked_empty_body),
                onRetry = viewModel::load,
            ) { blocked ->
                FlortioCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = blocked.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        TextButton(onClick = { viewModel.unblock(blocked.id) }) {
                            Text(
                                text = stringResource(R.string.blocked_unblock),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------ Hesap silme

@Composable
fun DeleteAccountScreen(
    onBack: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: DeleteAccountViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.submit) {
        if (state.submit is SubmitState.Success) onDeleted()
    }

    ScreenScaffold(title = stringResource(R.string.delete_title), onBack = onBack) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
            Column {
                Text(
                    text = stringResource(R.string.delete_warning_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                )
                Text(
                    text = stringResource(R.string.delete_warning_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SubmitErrorBanner(state.submit, Modifier.padding(bottom = 16.dp))
        FlortioTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = stringResource(R.string.field_password),
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = viewModel::submit,
            errorText = state.passwordError?.let { stringResource(it.messageRes()) },
        )
        Spacer(Modifier.height(8.dp))
        CheckboxRow(
            checked = state.confirmed,
            onCheckedChange = viewModel::onConfirmedChange,
            label = stringResource(R.string.delete_confirm_check),
            errorText = state.confirmError?.let { stringResource(it.messageRes()) },
        )
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = stringResource(R.string.delete_button),
            onClick = viewModel::submit,
            loading = state.submit is SubmitState.Loading,
            destructive = true,
        )
        Spacer(Modifier.height(16.dp))
    }
}
