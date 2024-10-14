package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result

interface AgendaRepository {
    suspend fun logout() :Result<Unit, DataError.Network>
}