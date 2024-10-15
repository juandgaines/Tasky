package com.juandgaines.core.data.auth

import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.AuthCoreService
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.EmptyDataResult
import com.juandgaines.core.domain.util.Result
import javax.inject.Inject

class DefaultAuthCoreService @Inject constructor(
    private val tokenApi: TokenApi,
    private val sessionManager: SessionManager
):AuthCoreService{
    override suspend fun logout(): EmptyDataResult<Network> {
        val response = safeCall {
            tokenApi.logout()
        }
        if (response is Result.Success){
            sessionManager.set(null)
        }
        return response
    }
}