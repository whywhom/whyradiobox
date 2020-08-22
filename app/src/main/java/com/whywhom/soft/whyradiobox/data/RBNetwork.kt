package com.whywhom.soft.whyradiobox.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by wuhaoyong on 22/08/20.
 */
class RBNetwork {
    private val mainService = NetworkModule.provideRetrofitService()

    suspend fun fetchToprank(lang: String, limit: String) = mainService.getTopList(lang, limit).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: RBNetwork? = null

        fun getInstance(): RBNetwork {
            if (network == null) {
                synchronized(RBNetwork::class.java) {
                    if (network == null) {
                        network = RBNetwork()
                    }
                }
            }
            return network!!
        }
    }
}