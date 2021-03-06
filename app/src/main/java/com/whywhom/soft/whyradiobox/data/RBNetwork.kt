package com.whywhom.soft.whyradiobox.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response

/**
 * Created by wuhaoyong on 22/08/20.
 */
class RBNetwork {

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