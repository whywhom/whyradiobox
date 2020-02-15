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
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {
    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

    @ColumnInfo(name = "url")
    var url: String = ""

    @ColumnInfo(name = "filepath")
    var filepath: String = ""

    @ColumnInfo(name = "coverurl")
    var coverurl: String = ""
}