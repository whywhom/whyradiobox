package com.whywhom.soft.whyradiobox.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response

class RBRepository private constructor(private val rbNetwork: RBNetwork){

    suspend fun refreshToprank(lang: String, limit: String) = rbNetwork.fetchToprank(lang, limit)

    companion object {

        private var repository: RBRepository? = null

        fun getInstance(network: RBNetwork): RBRepository {
            if (repository == null) {
                synchronized(RBRepository::class.java) {
                    if (repository == null) {
                        repository = RBRepository(network)
                    }
                }
            }

            return repository!!
        }
    }
}
