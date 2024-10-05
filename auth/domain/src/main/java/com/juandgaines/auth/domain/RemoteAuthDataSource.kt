package com.juandgaines.auth.domain

import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result

interface RemoteAuthDataSource {
    suspend fun login(email: String, password: String): Result<AuthData, DataError.Network>
    suspend fun register(fullName:String,email: String, password: String): Result<Unit, DataError.Network>
    suspend fun logout(): Result<Unit, DataError.Network>
}