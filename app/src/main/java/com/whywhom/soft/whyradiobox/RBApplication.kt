package com.whywhom.soft.whyradiobox

import android.app.Application
import android.content.Context
import android.os.Build
import com.whywhom.soft.whyradiobox.utils.Connectivity


/**
 * Created by wuhaoyong on 2020-01-10.
 */
open class RBApplication: Application() {
    companion object {
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        registerNetworkCallback()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun registerNetworkCallback(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Connectivity.registerNetworkCallback(this)
        }
    }
}