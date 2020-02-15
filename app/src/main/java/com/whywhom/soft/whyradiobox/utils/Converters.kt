package com.whywhom.soft.whyradiobox.utils

import android.text.TextUtils
import androidx.room.TypeConverter

/**
 * Created by wuhaoyong on 2020-02-15.
 */
class Converters {
    @TypeConverter
    open fun arrayToString(array: Array<String>): String {
        if (array == null || array.size === 0) {
            return ""
        }

        val builder = StringBuilder(array[0])
        for (i in 1..array.size - 1) {
            builder.append(",").append(array[i])
        }
        return builder.toString()
    }

    @TypeConverter
    open fun StringToArray(value: String): Array<String>? {
        return if (TextUtils.isEmpty(value)) null else value.split(",").toTypedArray()

    }
}