package com.whywhom.soft.whyradiobox.data.source.local

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_UNDOWNLOAD
import com.whywhom.soft.whyradiobox.utils.Converters
import java.util.*

/**
 * Created by wuhaoyong on 28/04/20.
 */

@Entity(tableName = "feeditem")
@TypeConverters(Converters::class)
data class FeedItem @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemid")
    var id: Int
) {

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "downloadurl")
    var downloadUrl: String = ""

    @ColumnInfo(name = "mediatype")
    var mediaType: String = ""

    @ColumnInfo(name = "length")
    var length: Int = 0

    @ColumnInfo(name = "duration")
    var duration: String = ""

    @ColumnInfo(name = "filepath")
    var filepath: String = ""

    //belong to whitch rsstag?
    @ColumnInfo(name = "belongto")
    var belongto: String = ""

    @ColumnInfo(name = "trackid")
    var trackId: String = ""

    @ColumnInfo(name = "pubdata")
    var pubData: String = ""

    @ColumnInfo(name = "control")
    var controlType: Int= CONTROL_TYPE_UNDOWNLOAD

}