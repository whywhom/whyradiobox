package com.whywhom.soft.whyradiobox.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PodcastDao {
    @Query("SELECT * FROM podcast")
    fun getAll(): List<Podcast>

    /**
     * add Podcast
     */
    @Insert
    fun updatePodcast(podcast: Podcast)
}
