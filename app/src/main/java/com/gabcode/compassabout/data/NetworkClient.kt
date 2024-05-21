package com.gabcode.compassabout.data

import java.io.IOException
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

interface IAboutService {
    suspend fun makeRequest(): AboutResponse
}

class NetworkClient : IAboutService {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }).build()
    }

    override suspend fun makeRequest(): AboutResponse {
        return suspendCoroutine { continuation ->

            val request = Request.Builder()
                .url(BASE_URL)
                .header("Accept", "text/plain; charset=utf-8")
                .build()

            val call = client.newCall(request)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) =
                    continuation.resumeWithException(e)

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            continuation.resumeWithException(IOException("Unexpected code ${response.code}"))
                            return
                        }

                        response.body?.let {
                            val aboutResponse = AboutResponse(it.string())
                            continuation.resumeWith(Result.success(aboutResponse))
                        } ?: throw IOException("Invalid response body")
                    } catch (e: IOException) {
                        continuation.resumeWithException(e)
                    }
                }
            })
        }
    }

    companion object {
        private const val BASE_URL = "https://www.compass.com/about"
    }
}
