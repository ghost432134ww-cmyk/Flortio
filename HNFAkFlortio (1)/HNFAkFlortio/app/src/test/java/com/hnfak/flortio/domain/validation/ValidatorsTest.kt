package com.hnfak.flortio.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    @Test
    fun email_rules() {
        assertEquals(FieldError.EmailEmpty, Validators.email("  "))
        assertEquals(FieldError.EmailInvalid, Validators.email("ali@"))
        assertEquals(FieldError.EmailInvalid, Validators.email("ali@site"))
        assertNull(Validators.email(" ali@site.com "))
    }

    @Test
    fun new_password_rules() {
        assertEquals(FieldError.PasswordEmpty, Validators.newPassword(""))
        assertEquals(FieldError.PasswordTooShort, Validators.newPassword("abc123"))
        assertEquals(FieldError.PasswordWeak, Validators.newPassword("abcdefgh"))
        assertEquals(FieldError.PasswordWeak, Validators.newPassword("12345678"))
        assertNull(Validators.newPassword("abcd1234"))
    }

    @Test
    fun confirm_password_rules() {
        assertEquals(FieldError.ConfirmEmpty, Validators.confirmPassword("abcd1234", ""))
        assertEquals(FieldError.ConfirmMismatch, Validators.confirmPassword("abcd1234", "abcd12345"))
        assertNull(Validators.confirmPassword("abcd1234", "abcd1234"))
    }

    @Test
    fun birth_year_rules() {
        assertEquals(FieldError.BirthYearEmpty, Validators.birthYear("", 2026))
        assertEquals(FieldError.BirthYearInvalid, Validators.birthYear("abcd", 2026))
        assertEquals(FieldError.BirthYearInvalid, Validators.birthYear("2030", 2026))
        assertEquals(FieldError.BirthYearInvalid, Validators.birthYear("1800", 2026))
        assertEquals(FieldError.Underage, Validators.birthYear("2012", 2026))
        assertNull(Validators.birthYear("2000", 2026))
    }

    @Test
    fun profile_text_rules() {
        assertEquals(FieldError.NameEmpty, Validators.displayName(" "))
        assertEquals(FieldError.NameLength, Validators.displayName("A"))
        assertNull(Validators.displayName("Ayşe"))
        assertEquals(FieldError.BioTooLong, Validators.bio("x".repeat(301)))
        assertNull(Validators.bio("x".repeat(300)))
        assertEquals(FieldError.CityTooLong, Validators.city("x".repeat(41)))
    }

    @Test
    fun checkbox_rule() {
        assertEquals(FieldError.NotConfirmed, Validators.mustBeChecked(false))
        assertNull(Validators.mustBeChecked(true))
    }
}
