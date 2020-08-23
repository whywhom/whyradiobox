package com.whywhom.soft.whyradiobox.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response

/**
 * Created by wuhaoyong on 22/08/20.
 */
class RBNetwork {
    private val mainService = NetworkModule.provideOKhttpService()

    suspend fun fetchToprank(lang: String, limit: String): Response{
        return withContext(Dispatchers.IO) {
            val topUrl =
                NetworkModule.itunesBaseUrl + lang + "/rss/toppodcasts/limit=" + limit + "/explicit=true/json";
            val request = Request.Builder().get()
                .url(topUrl)
                .build()

                var response = mainService.newCall(request).execute()

            return@withContext response
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