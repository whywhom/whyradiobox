package com.whywhom.soft.whyradiobox.di.module

import android.content.Context
import androidx.room.Room
import com.whywhom.soft.whyradiobox.data.source.DefaultRBRepository
import com.whywhom.soft.whyradiobox.data.source.RBRepository
import com.whywhom.soft.whyradiobox.data.source.TasksDataSource
import com.whywhom.soft.whyradiobox.data.source.local.LocalDataSource
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase
import com.whywhom.soft.whyradiobox.data.source.remote.RemoteDataSource
import com.whywhom.soft.whyradiobox.utils.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by wuhaoyong on 2020-01-12.
 */
@Module(includes = [ApplicationModuleBinds::class, DbModule::class])
object ApplicationModule {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDataSource

    @JvmStatic
    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(): TasksDataSource {
        return RemoteDataSource
    }

    @JvmStatic
    @Singleton
    @LocalDataSource
    @Provides
    fun provideLocalDataSource(
        database: PodcastDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TasksDataSource {
        return LocalDataSource(
            database.podcastDao(),
            ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): PodcastDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PodcastDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultRBRepository): RBRepository
}
