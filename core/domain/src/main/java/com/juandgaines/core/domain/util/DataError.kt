package com.juandgaines.core.domain.util

sealed interface DataError: Error {

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        UNAUTHORIZED,
        FORBIDDEN,
        BAD_REQUEST,
        SERIALIZATION,
        CONFLICT,
        TOO_MANY_REQUESTS,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        UNKNOWN
    }

    enum class LocalError: DataError {
        DISK_FULL,
        NOT_FOUND,
        FILE_ERROR,
        UNKNOWN
    }
}