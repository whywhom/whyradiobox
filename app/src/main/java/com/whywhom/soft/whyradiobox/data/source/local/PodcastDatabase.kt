package com.whywhom.soft.whyradiobox.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whywhom.soft.whyradiobox.data.source.Podcast

@Database(entities = [Podcast::class], version = 1, exportSchema = false)
abstract class PodcastDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao
}