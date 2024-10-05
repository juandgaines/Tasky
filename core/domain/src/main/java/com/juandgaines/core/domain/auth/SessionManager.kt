package com.juandgaines.core.domain.auth

import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface SessionManager {
    suspend fun get(): AuthData?
    suspend fun set(data: AuthData?)
    suspend fun refresh(): Result<AuthData?, Network>
    suspend fun checkAuth():Result<Unit,Network>
}