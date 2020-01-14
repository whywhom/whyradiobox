package com.whywhom.soft.whyradiobox.data.source.local

import com.whywhom.soft.whyradiobox.data.source.TasksDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by wuhaoyong on 2020-01-11.
 */
class LocalDataSource internal constructor(
    private val postcastDao: PodcastDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksDataSource {
}