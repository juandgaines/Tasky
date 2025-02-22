package com.juandgaines.core.domain.util

sealed interface Result<out D, out E: Error>{
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E: com.juandgaines.core.domain.util.Error>(val error: E) : Result<Nothing, E>
}

inline fun <T,E:Error,R> Result<T,E>.map(map: (T) -> R): Result<R,E> {
    return when (this) {
        is Result.Success -> Result.Success(map(this.data))
        is Result.Error -> Result.Error(this.error)
    }
}

inline fun <T, E: Error, R: Error> Result<T, E>.mapError(map: (E) -> R): Result<T, R> {
    return when (this) {
        is Result.Error -> Result.Error(map(error))
        is Result.Success -> Result.Success(data)
    }
}

inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

fun <T, E:Error> Result<T,E>.asEmptyDataResult(): Result<Unit, E> {
    return map {
        when (this) {
            is Result.Success -> Unit
            is Result.Error -> error
        }
    }
}

typealias EmptyDataResult<E> = Result<Unit, E>


