package com.whywhom.soft.whyradiobox.utils

import android.content.Context
import android.text.TextUtils
import androidx.room.TypeConverter
import com.whywhom.soft.whyradiobox.R
import java.util.*


/**
 * Created by wuhaoyong on 2020-02-15.
 */
class Converters {
    private val HOURS_MIL = 3600000
    private val MINUTES_MIL = 60000
    private val SECONDS_MIL = 1000

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

    /** Converts milliseconds to a string containing hours, minutes and seconds  */
    fun getDurationStringLong(duration: Int): String? {
        val h = duration / HOURS_MIL
        var rest = duration - h * HOURS_MIL
        val m = rest / MINUTES_MIL
        rest -= m * MINUTES_MIL
        val s = rest / SECONDS_MIL
        return java.lang.String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
    }

    /** Converts milliseconds to a string containing hours and minutes or minutes and seconds */
    fun getDurationStringShort(
        duration: Int,
        durationIsInHours: Boolean
    ): String? {
        val firstPartBase = if (durationIsInHours) HOURS_MIL else MINUTES_MIL
        val firstPart = duration / firstPartBase
        val leftoverFromFirstPart = duration - firstPart * firstPartBase
        val secondPart =
            leftoverFromFirstPart / if (durationIsInHours) MINUTES_MIL else SECONDS_MIL
        return java.lang.String.format(Locale.getDefault(), "%02d:%02d", firstPart, secondPart)
    }

    /** Converts long duration string (HH:MM:SS) to milliseconds.  */
    fun durationStringLongToMs(input: String): Int {
        val parts = input.split(":").toTypedArray()
        return if (parts.size != 3) {
            0
        } else parts[0].toInt() * 3600 * 1000 + parts[1].toInt() * 60 * 1000 + parts[2].toInt() * 1000
    }

    /**
     * Converts short duration string (XX:YY) to milliseconds. If durationIsInHours is true then the
     * format is HH:MM, otherwise it's MM:SS.
     */
    fun durationStringShortToMs(input: String, durationIsInHours: Boolean): Int {
        val parts = input.split(":").toTypedArray()
        if (parts.size != 2) {
            return 0
        }
        val modifier = if (durationIsInHours) 60 else 1
        return parts[0].toInt() * 60 * 1000 * modifier +
                parts[1].toInt() * 1000 * modifier
    }

    /** Converts milliseconds to a localized string containing hours and minutes  */
    fun getDurationStringLocalized(context: Context, duration: Long): String? {
        val h = (duration / HOURS_MIL).toInt()
        val rest = (duration - h * HOURS_MIL).toInt()
        val m = rest / MINUTES_MIL
        var result: String? = ""
        if (h > 0) {
            val hours: String =
                context.getResources().getQuantityString(R.plurals.time_hours_quantified, h, h)
            result += "$hours "
        }
        val minutes: String =
            context.getResources().getQuantityString(R.plurals.time_minutes_quantified, m, m)
        result += minutes
        return result
    }

    /**
     * Converts seconds to a localized representation
     * @param time The time in seconds
     * @return "HH:MM hours"
     */
    fun shortLocalizedDuration(context: Context, time: Long): String? {
        val hours = time.toFloat() / 3600f
        return java.lang.String.format(
            Locale.getDefault(),
            "%.1f ",
            hours
        ) + context.getString(R.string.time_hours)
    }


    /**
     * Converts the volume as read as the progress from a SeekBar scaled to 100 and as saved in
     * UserPreferences to the format taken by setVolume methods.
     * @param progress integer between 0 to 100 taken from the SeekBar progress
     * @return the appropriate volume as float taken by setVolume methods
     */
    fun getVolumeFromPercentage(progress: Int): Float {
        return if (progress == 100) 1f else (1 - Math.log(101 - progress.toDouble()) / Math.log(
            101.0
        )).toFloat()
    }
}