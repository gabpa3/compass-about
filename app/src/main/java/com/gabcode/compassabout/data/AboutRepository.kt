package com.gabcode.compassabout.data

import android.util.Log
import com.gabcode.compassabout.data.database.AboutDao
import com.gabcode.compassabout.data.database.AboutEntity
import com.gabcode.compassabout.util.ConnectivityChecker
import com.gabcode.compassabout.util.NotFoundConnectivityException
import com.gabcode.compassabout.util.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface IAboutRepository {
    suspend fun fetchContent(): Result<AboutResponse>
}
class AboutRepository(
    private val service: IAboutService,
    private val connectivityChecker: ConnectivityChecker,
    private val aboutDao: AboutDao,
    private val ioDispatcher: CoroutineDispatcher
) : IAboutRepository {

    override suspend fun fetchContent(): Result<AboutResponse> = withContext(ioDispatcher) {
        val result = getCachedData()?.let { entity ->
            AboutResponse(entity.content)
        } ?: getRemoteData()

        result.asResult()
    }

    private suspend fun getRemoteData(): AboutResponse {
        if (!connectivityChecker.isNetworkAvailable()) {
            throw NotFoundConnectivityException()
        }

        return service.makeRequest().also { response ->
            saveRemoteData(response)
        }
    }

    private suspend fun saveRemoteData(response: AboutResponse) {
        aboutDao.insert(content = AboutEntity(content = response.content))
        Log.i("AboutRepository", "Data saved to database")
    }

    private suspend fun getCachedData(): AboutEntity? = aboutDao.getData()
}
