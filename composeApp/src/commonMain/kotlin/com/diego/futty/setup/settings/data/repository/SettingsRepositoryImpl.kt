package com.diego.futty.setup.settings.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.setup.settings.data.network.RemoteLogoutDataSource
import com.diego.futty.setup.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val remoteLogoutDataSource: RemoteLogoutDataSource
) : SettingsRepository {

    override suspend fun logout(): DataResult<String, DataError.Remote> {
        return remoteLogoutDataSource.logout()
    }
}