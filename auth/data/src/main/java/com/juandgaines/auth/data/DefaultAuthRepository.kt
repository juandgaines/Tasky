package com.juandgaines.auth.data

import com.juandgaines.auth.domain.AuthRepository
import com.juandgaines.auth.domain.RemoteAuthDataSource
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val sessionStorage: SessionManager
):AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit, Network> {
        val result = remoteAuthDataSource.login(email, password)
        if (result is Result.Success) {
            sessionStorage.set(result.data)
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(
        fullName:String,
        email: String,
        password: String
    ): Result<Unit, Network> {
        val result = remoteAuthDataSource.register(
            fullName = fullName,
            email = email,
            password = password
        )
        return result.asEmptyDataResult()
    }
}