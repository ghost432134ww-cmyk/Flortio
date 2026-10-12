package com.hnfak.flortio.domain

/** Bir işlemin neden başarısız olduğunu UI'a bildiren hata türleri. */
sealed interface AppError {
    /** Gerçek bir backend (ör. Firebase) henüz bağlanmadı. */
    data object NotConfigured : AppError
    data object InvalidCredentials : AppError
    data object EmailInUse : AppError
    data object Network : AppError
    data object NotSignedIn : AppError
    data class Unknown(val detail: String? = null) : AppError
}

/** Repository çağrılarının sonucu: ya değer ya da açık bir hata. */
sealed interface DataResult<out T> {
    data class Success<out T>(val value: T) : DataResult<T>
    data class Failure(val error: AppError) : DataResult<Nothing>
}
