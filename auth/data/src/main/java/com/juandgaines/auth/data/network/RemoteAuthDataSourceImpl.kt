package com.juandgaines.auth.data.network

import com.juandgaines.auth.data.network.login.LoginRequest
import com.juandgaines.auth.data.network.register.RegistrationRequest
import com.juandgaines.auth.domain.RemoteAuthDataSource
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import javax.inject.Inject

class RemoteAuthDataSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : RemoteAuthDataSource {
    override suspend fun login(
        email: String,
        password: String,
    ): Result<AuthData, Network> {
        return safeCall {
            authApi.login(LoginRequest(email, password))
        }.map {
            AuthData(
                it.accessToken,
                it.refreshToken,
                it.userId
            )
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): Result<Unit, Network> {
        return safeCall {
            authApi.register(
                RegistrationRequest(
                    fullName = fullName,
                    email = email,
                    password = password,
                )
            )
        }
    }

    override suspend fun logout(): Result<Unit, Network> {
        return safeCall {
            authApi.logout()
        }
    }

    override suspend fun refreshToken(): Result<Unit, Network> {
        return safeCall {
            authApi.refreshToken()
        }
    }

    override suspend fun checkAuth(): Result<Unit, Network> {
        return safeCall {
            authApi.checkAuth()
        }
    }
}