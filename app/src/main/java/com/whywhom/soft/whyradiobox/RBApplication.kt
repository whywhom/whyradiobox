package com.whywhom.soft.whyradiobox

import com.whywhom.soft.whyradiobox.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


/**
 * Created by wuhaoyong on 2020-01-10.
 */
open class RBApplication: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }
}