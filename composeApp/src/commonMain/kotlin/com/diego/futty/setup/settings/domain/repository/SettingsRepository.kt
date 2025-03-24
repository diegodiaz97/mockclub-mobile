package com.diego.futty.setup.settings.domain.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface SettingsRepository {
    suspend fun logout(): DataResult<String, DataError.Remote>
}