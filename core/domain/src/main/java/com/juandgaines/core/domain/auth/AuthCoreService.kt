package com.juandgaines.core.domain.auth

import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.EmptyDataResult

interface AuthCoreService {
    suspend fun logout():EmptyDataResult<DataError.Network>
}