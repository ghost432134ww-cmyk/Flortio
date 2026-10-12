package com.hnfak.flortio.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hnfak.flortio.R
import com.hnfak.flortio.ui.common.AppViewModelProvider
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.messageRes
import com.hnfak.flortio.ui.components.BannerKind
import com.hnfak.flortio.ui.components.FlortioTextField
import com.hnfak.flortio.ui.components.PrimaryButton
import com.hnfak.flortio.ui.components.ScreenScaffold
import com.hnfak.flortio.ui.components.StatusBanner
import com.hnfak.flortio.ui.components.SubmitErrorBanner

@Composable
private fun AuthHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------- Giriş

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.submit) {
        if (state.submit is SubmitState.Success) onSuccess()
    }

    ScreenScaffold(title = "", onBack = onBack) {
        AuthHeader(
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle),
        )
        SubmitErrorBanner(state.submit, Modifier.padding(bottom = 16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FlortioTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = stringResource(R.string.field_email),
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                errorText = state.emailError?.let { stringResource(it.messageRes()) },
            )
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
        }
        TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.End)) {
            Text(
                text = stringResource(R.string.login_forgot),
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Spacer(Modifier.height(8.dp))
        PrimaryButton(
            text = stringResource(R.string.login_button),
            onClick = viewModel::submit,
            loading = state.submit is SubmitState.Loading,
        )
        Spacer(Modifier.height(16.dp))
        AuthSwitchRow(
            question = stringResource(R.string.login_no_account),
            action = stringResource(R.string.login_go_register),
            onClick = onRegister,
        )
    }
}

@Composable
private fun AuthSwitchRow(question: String, action: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = onClick) {
            Text(text = action, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

// ---------------------------------------------------------------- Kayıt

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.submit) {
        if (state.submit is SubmitState.Success) onSuccess()
    }

    ScreenScaffold(title = "", onBack = onBack) {
        AuthHeader(
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle),
        )
        SubmitErrorBanner(state.submit, Modifier.padding(bottom = 16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FlortioTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = stringResource(R.string.field_email),
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                errorText = state.emailError?.let { stringResource(it.messageRes()) },
            )
            FlortioTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = stringResource(R.string.field_password),
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                supportingText = stringResource(R.string.register_password_hint),
                errorText = state.passwordError?.let { stringResource(it.messageRes()) },
            )
            FlortioTextField(
                value = state.confirmation,
                onValueChange = viewModel::onConfirmationChange,
                label = stringResource(R.string.field_password_confirm),
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                imeAction = ImeAction.Done,
                onImeAction = viewModel::submit,
                errorText = state.confirmationError?.let { stringResource(it.messageRes()) },
            )
        }
        Spacer(Modifier.height(8.dp))
        CheckboxRow(
            checked = state.ageConfirmed,
            onCheckedChange = viewModel::onAgeConfirmedChange,
            label = stringResource(R.string.register_age_confirm),
            errorText = state.ageError?.let { stringResource(it.messageRes()) },
        )
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = stringResource(R.string.register_button),
            onClick = viewModel::submit,
            loading = state.submit is SubmitState.Loading,
        )
        Spacer(Modifier.height(16.dp))
        AuthSwitchRow(
            question = stringResource(R.string.register_have_account),
            action = stringResource(R.string.register_go_login),
            onClick = onLogin,
        )
    }
}

/** Satırın tamamı tıklanabilir, erişilebilir onay kutusu. */
@Composable
fun CheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    errorText: String? = null,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

// ------------------------------------------------------- Şifre sıfırlama

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sent = state.submit is SubmitState.Success

    ScreenScaffold(title = "", onBack = onBack) {
        AuthHeader(
            title = stringResource(R.string.forgot_title),
            subtitle = stringResource(R.string.forgot_subtitle),
        )
        if (sent) {
            StatusBanner(
                text = stringResource(R.string.forgot_success),
                kind = BannerKind.Success,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            PrimaryButton(text = stringResource(R.string.forgot_back_to_login), onClick = onBack)
        } else {
            SubmitErrorBanner(state.submit, Modifier.padding(bottom = 16.dp))
            FlortioTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = stringResource(R.string.field_email),
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                onImeAction = viewModel::submit,
                errorText = state.emailError?.let { stringResource(it.messageRes()) },
            )
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.forgot_button),
                onClick = viewModel::submit,
                loading = state.submit is SubmitState.Loading,
            )
        }
    }
}
