package com.whywhom.soft.whyradiobox.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RBRepository private constructor(private val rbNetwork: RBNetwork){

    suspend fun refreshToprank(lang: String, limit: String) = requestToprank(lang, limit)

    private suspend fun requestToprank(lang: String, limit: String) = withContext(Dispatchers.IO) {
        val response = rbNetwork.fetchToprank(lang, limit)
        response
    }

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
