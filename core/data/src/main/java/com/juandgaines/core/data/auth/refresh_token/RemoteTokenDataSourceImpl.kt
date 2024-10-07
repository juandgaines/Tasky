package com.juandgaines.core.data.auth.refresh_token

import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.auth.refresh_token.RemoteTokenDataSource
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import javax.inject.Inject

class RemoteTokenDataSourceImpl @Inject constructor(
    private val api: TokenApi
):RemoteTokenDataSource {
    override suspend fun refreshToken(
        authData: AuthData,
    ): Result<AuthData, Network> = safeCall {
        api.refreshToken(
            RefreshTokenRequest(
                authData.refreshToken,
                authData.userId
            )
        )
    }.map {
        val newAuthData = AuthData(
            accessToken = it.accessToken,
            fullName = authData.fullName,
            accessTokenExpirationTimestamp = it.expirationTimestamp,
            refreshToken = authData.refreshToken,
            userId = authData.userId
        )
        newAuthData
    }


    override suspend fun checkAuth(): Result<Unit, Network> = safeCall {
        api.checkAuth()
    }

    override suspend fun logOut(): Result<Unit, Network> = safeCall {
        api.logout()
    }
}