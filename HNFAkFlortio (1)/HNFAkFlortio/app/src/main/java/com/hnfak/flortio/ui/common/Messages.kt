package com.hnfak.flortio.ui.common

import androidx.annotation.StringRes
import com.hnfak.flortio.R
import com.hnfak.flortio.domain.AppError
import com.hnfak.flortio.domain.validation.FieldError

/** Alan doğrulama hatalarının Türkçe karşılıkları. */
@StringRes
fun FieldError.messageRes(): Int = when (this) {
    FieldError.EmailEmpty -> R.string.err_email_empty
    FieldError.EmailInvalid -> R.string.err_email_invalid
    FieldError.PasswordEmpty -> R.string.err_password_empty
    FieldError.PasswordTooShort -> R.string.err_password_short
    FieldError.PasswordWeak -> R.string.err_password_weak
    FieldError.ConfirmEmpty -> R.string.err_confirm_empty
    FieldError.ConfirmMismatch -> R.string.err_confirm_mismatch
    FieldError.NotConfirmed -> R.string.err_not_confirmed
    FieldError.NameEmpty -> R.string.err_name_empty
    FieldError.NameLength -> R.string.err_name_length
    FieldError.BioTooLong -> R.string.err_bio_long
    FieldError.CityTooLong -> R.string.err_city_long
    FieldError.BirthYearEmpty -> R.string.err_birth_empty
    FieldError.BirthYearInvalid -> R.string.err_birth_invalid
    FieldError.Underage -> R.string.err_birth_underage
}

/** İşlem hatalarının Türkçe karşılıkları. */
@StringRes
fun AppError.messageRes(): Int = when (this) {
    AppError.NotConfigured -> R.string.error_not_configured
    AppError.InvalidCredentials -> R.string.error_invalid_credentials
    AppError.EmailInUse -> R.string.error_email_in_use
    AppError.Network -> R.string.error_network
    AppError.NotSignedIn -> R.string.error_not_signed_in
    is AppError.Unknown -> R.string.error_unknown
}
