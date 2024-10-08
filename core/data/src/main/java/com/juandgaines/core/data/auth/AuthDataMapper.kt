package com.juandgaines.core.data.auth

import com.juandgaines.core.domain.AuthData

fun AuthDataDto.toAuthData(): AuthData {
    return AuthData(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        accessTokenExpirationTimestamp = accessTokenExpirationTimestamp,
        fullName = fullName
    )
}

fun AuthData.toAuthDataDto(): AuthDataDto {
    return AuthDataDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        accessTokenExpirationTimestamp = accessTokenExpirationTimestamp,
        fullName = fullName
    )
}