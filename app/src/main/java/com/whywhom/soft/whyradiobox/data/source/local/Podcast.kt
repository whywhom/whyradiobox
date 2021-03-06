package com.whywhom.soft.whyradiobox.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.whywhom.soft.whyradiobox.utils.Converters
import java.util.*

@Entity(tableName = "podcast")
@TypeConverters(Converters::class)
data class Podcast @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entryid")
    var id: Int
) {
    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "trackid")
    var trackId: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

    @ColumnInfo(name = "url")
    var url: String = ""

    @ColumnInfo(name = "rssurl")
    var rssurl: String = ""

    @ColumnInfo(name = "rsstag")
    var rsstag: String = ""

    @ColumnInfo(name = "filepath")
    var filepath: String = ""

    @ColumnInfo(name = "coverurl")
    var coverurl: String = ""

    @ColumnInfo(name = "download")
    var downloadState : Int = 0 //0: online; 1: downloaded; 2:downloading; 3:pause

    @ColumnInfo(name = "contentlength")
    var contentLength: Long = 0

    @ColumnInfo(name = "currentbytes")
    var currentBytes: Long = 0
}