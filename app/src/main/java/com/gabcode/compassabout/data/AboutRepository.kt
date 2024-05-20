package com.gabcode.compassabout.data

import android.util.Log
import com.gabcode.compassabout.data.database.AboutDao
import com.gabcode.compassabout.data.database.AboutEntity
import com.gabcode.compassabout.util.ConnectivityChecker
import com.gabcode.compassabout.util.NotFoundConnectivityException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface IAboutRepository {
    suspend fun fetchContent(): Result<AboutResponse>
}

/**
 * Repository responsible of fetching data from remote or local database. Moreover, it checks
 * connectivity availability.
 *
 * @property service [IAboutService]
 * @property connectivityChecker [ConnectivityChecker]
 * @property aboutDao [AboutDao]
 * @property ioDispatcher CoroutineDispatcher
 */
class AboutRepository(
    private val service: IAboutService,
    private val connectivityChecker: ConnectivityChecker,
    private val aboutDao: AboutDao,
    private val ioDispatcher: CoroutineDispatcher
) : IAboutRepository {

    override suspend fun fetchContent(): Result<AboutResponse> = withContext(ioDispatcher) {
        try {
            val result = getCachedData()?.let { entity ->
                AboutResponse(entity.content)
            } ?: getRemoteData()

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
