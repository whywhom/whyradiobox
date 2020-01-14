package com.whywhom.soft.whyradiobox.di.component


import android.content.Context
import com.whywhom.soft.whyradiobox.RBApplication

import com.whywhom.soft.whyradiobox.di.module.ApplicationModule
import com.whywhom.soft.whyradiobox.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component

import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

import javax.inject.Singleton

/**
 * Created by wuhaoyong on 2020-01-10.
 */
// Definition of the Application graph

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    ApplicationModule::class])
interface ApplicationComponent: AndroidInjector<RBApplication> {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}