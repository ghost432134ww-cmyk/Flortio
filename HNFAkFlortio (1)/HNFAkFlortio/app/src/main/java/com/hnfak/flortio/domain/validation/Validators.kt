package com.hnfak.flortio.domain.validation

/** Form doğrulama hataları. UI katmanı bunları Türkçe metne çevirir. */
enum class FieldError {
    EmailEmpty,
    EmailInvalid,
    PasswordEmpty,
    PasswordTooShort,
    PasswordWeak,
    ConfirmEmpty,
    ConfirmMismatch,
    NotConfirmed,
    NameEmpty,
    NameLength,
    BioTooLong,
    CityTooLong,
    BirthYearEmpty,
    BirthYearInvalid,
    Underage,
}

object Validators {
    const val MIN_PASSWORD_LENGTH = 8
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 30
    const val MAX_BIO_LENGTH = 300
    const val MAX_CITY_LENGTH = 40
    const val MIN_AGE = 18
    private const val MAX_AGE = 120

    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")

    fun email(raw: String): FieldError? {
        val value = raw.trim()
        return when {
            value.isEmpty() -> FieldError.EmailEmpty
            !EMAIL_REGEX.matches(value) -> FieldError.EmailInvalid
            else -> null
        }
    }

    /** Girişte yalnızca boş olup olmadığına bakılır; kuralları sunucu belirler. */
    fun requiredPassword(value: String): FieldError? =
        if (value.isEmpty()) FieldError.PasswordEmpty else null

    fun newPassword(value: String): FieldError? = when {
        value.isEmpty() -> FieldError.PasswordEmpty
        value.length < MIN_PASSWORD_LENGTH -> FieldError.PasswordTooShort
        !value.any { it.isLetter() } || !value.any { it.isDigit() } -> FieldError.PasswordWeak
        else -> null
    }

    fun confirmPassword(password: String, confirmation: String): FieldError? = when {
        confirmation.isEmpty() -> FieldError.ConfirmEmpty
        password != confirmation -> FieldError.ConfirmMismatch
        else -> null
    }

    fun mustBeChecked(checked: Boolean): FieldError? =
        if (checked) null else FieldError.NotConfirmed

    fun displayName(raw: String): FieldError? {
        val length = raw.trim().length
        return when {
            length == 0 -> FieldError.NameEmpty
            length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH -> FieldError.NameLength
            else -> null
        }
    }

    fun bio(raw: String): FieldError? =
        if (raw.trim().length > MAX_BIO_LENGTH) FieldError.BioTooLong else null

    fun city(raw: String): FieldError? =
        if (raw.trim().length > MAX_CITY_LENGTH) FieldError.CityTooLong else null

    /**
     * Doğum yılı kontrolü. Yalnızca yıl bilindiği için yaş yaklaşıktır;
     * Firebase aşamasında tam doğum tarihi alınmalıdır.
     */
    fun birthYear(raw: String, currentYear: Int): FieldError? {
        val value = raw.trim()
        if (value.isEmpty()) return FieldError.BirthYearEmpty
        val year = value.toIntOrNull() ?: return FieldError.BirthYearInvalid
        return when {
            year > currentYear || currentYear - year > MAX_AGE -> FieldError.BirthYearInvalid
            currentYear - year < MIN_AGE -> FieldError.Underage
            else -> null
        }
    }
}
