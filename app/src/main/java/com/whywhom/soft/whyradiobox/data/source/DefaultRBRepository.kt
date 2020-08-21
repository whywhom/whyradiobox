package com.whywhom.soft.whyradiobox.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by wuhaoyong on 2020-01-11.
 */
class DefaultRBRepository (
//    @RemoteDataSource private val tasksRemoteDataSource: TasksDataSource,
//    @LocalDataSource private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RBRepository {
}