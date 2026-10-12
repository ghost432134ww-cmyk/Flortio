package com.hnfak.flortio.ui.common

import com.hnfak.flortio.domain.AppError
import com.hnfak.flortio.domain.DataResult

/** Bir formun gönderim durumu: boşta, yükleniyor, başarılı veya hatalı. */
sealed interface SubmitState {
    data object Idle : SubmitState
    data object Loading : SubmitState
    data object Success : SubmitState
    data class Failed(val error: AppError) : SubmitState
}

/** Liste ekranlarının durumu: yükleniyor, boş, hata veya içerik. */
sealed interface ListState<out T> {
    data object Loading : ListState<Nothing>
    data object Empty : ListState<Nothing>
    data class Error(val error: AppError) : ListState<Nothing>
    data class Content<out T>(val items: List<T>) : ListState<T>
}

fun DataResult<*>.toSubmitState(): SubmitState = when (this) {
    is DataResult.Success<*> -> SubmitState.Success
    is DataResult.Failure -> SubmitState.Failed(error)
}

fun <T> DataResult<List<T>>.toListState(): ListState<T> = when (this) {
    is DataResult.Success -> if (value.isEmpty()) ListState.Empty else ListState.Content(value)
    is DataResult.Failure -> ListState.Error(error)
}

/** Kullanıcı yazmaya devam ettiğinde eski hata bildirimini temizler. */
fun SubmitState.withoutFailure(): SubmitState =
    if (this is SubmitState.Failed) SubmitState.Idle else this
