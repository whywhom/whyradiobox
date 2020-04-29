package com.whywhom.soft.whyradiobox.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.whywhom.soft.whyradiobox.utils.DATABASE_NAME

@Database(entities = [Podcast::class, FeedItem::class], version = 1, exportSchema = false)
abstract class PodcastDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao
    abstract fun FeedItemDao(): FeedItemDao
    companion object {

        @Volatile private var INSTANCE: PodcastDatabase? = null
        private val sLock = Any()

        fun getInstance(context: Context): PodcastDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PodcastDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                }
                return INSTANCE as PodcastDatabase
            }
        }
    }
}