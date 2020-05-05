package com.whywhom.soft.whyradiobox.data.source.local

import androidx.room.*

@Dao
interface FeedItemDao {
    @Query("SELECT * FROM feeditem")
    fun getAll(): List<FeedItem>

    /**
     * add FeedItem
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFeedItems(vararg feedItem: FeedItem):List<Long>

    @Query("SELECT * FROM feeditem WHERE belongto = :podInfo")
    fun loadAllFeedItem(podInfo: String): List<FeedItem>

    @Delete
    fun deleteFeedItems(vararg feedItem: FeedItem)

}
