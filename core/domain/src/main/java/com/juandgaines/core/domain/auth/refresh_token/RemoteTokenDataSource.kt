package com.juandgaines.core.domain.auth.refresh_token

import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface RemoteTokenDataSource {
    suspend fun refreshToken(
        authData: AuthData
    ): Result<AuthData, Network>
}