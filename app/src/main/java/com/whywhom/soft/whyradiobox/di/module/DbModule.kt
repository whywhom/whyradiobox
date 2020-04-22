package com.whywhom.soft.whyradiobox.di.module

import com.whywhom.soft.whyradiobox.data.source.local.PodcastDao
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

/**
 * Created by wuhaoyong on 28/02/20.
 */

@Module
class DbModule {
    //用于注入PodcastDao
    @Singleton
    @Provides
    fun provideUserDao(podcastDatabase: PodcastDatabase): PodcastDao? {
        return podcastDatabase.podcastDao()
    }
}