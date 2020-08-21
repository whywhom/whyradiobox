package com.whywhom.soft.whyradiobox

import android.app.Application
import android.content.Context


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
}