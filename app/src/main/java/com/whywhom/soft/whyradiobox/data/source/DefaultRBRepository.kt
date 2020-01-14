package com.whywhom.soft.whyradiobox.data.source

import com.whywhom.soft.whyradiobox.di.module.ApplicationModule
import com.whywhom.soft.whyradiobox.di.module.ApplicationModule.LocalDataSource
import com.whywhom.soft.whyradiobox.di.module.ApplicationModule.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by wuhaoyong on 2020-01-11.
 */
class DefaultRBRepository @Inject constructor(
    @RemoteDataSource private val tasksRemoteDataSource: TasksDataSource,
    @LocalDataSource private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RBRepository {
}