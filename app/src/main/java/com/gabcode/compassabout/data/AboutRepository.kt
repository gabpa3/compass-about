package com.gabcode.compassabout.data

import com.gabcode.compassabout.data.database.AboutDao
import kotlinx.coroutines.CoroutineDispatcher

interface IAboutRepository {
    suspend fun fetchContent(): Result<AboutResponse>
}
class AboutRepository(
    private val service: IAboutService,
    private val aboutDao: AboutDao,
    private val ioDispatcher: CoroutineDispatcher
) : IAboutRepository {
    override suspend fun fetchContent(): Result<AboutResponse> {
        TODO("Not yet implemented")
    }
}
