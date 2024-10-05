package com.juandgaines.core.domain.auth

import com.juandgaines.core.domain.AuthData

interface SessionManager {

    suspend fun get(): AuthData?
    suspend fun set(data: AuthData?)
    suspend fun refresh(): AuthData?
    suspend fun checkAuth(): CheckAuthType

    sealed interface CheckAuthType{
        data object Valid : CheckAuthType
        data object Invalid : CheckAuthType
        data object Error : CheckAuthType
    }
}