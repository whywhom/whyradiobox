package com.whywhom.soft.whyradiobox.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeedItemDao {
    @Query("SELECT * FROM feeditem")
    fun getAll(): List<FeedItem>

    /**
     * add FeedItem
     */
    @Insert
    fun updateFeedItem(feedItem: FeedItem)
}
