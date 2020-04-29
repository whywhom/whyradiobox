package com.whywhom.soft.whyradiobox.utils

import java.math.BigDecimal

/**
 * Created by wuhaoyong on 2020-02-13.
 */

const val DATABASE_NAME = "podcast-db"

object Constants{
    const val CONTROL_TYPE_UNDOWNLOAD:Int = 0
    const val CONTROL_TYPE_DOWNLOAD:Int = 1
    const val CONTROL_TYPE_DOWNLOADING:Int = 2
    const val CONTROL_TYPE_DOWNLOADED:Int = 3
    const val CONTROL_TYPE_PLAYING:Int = 4
    const val CONTROL_TYPE_PAUSE:Int = 5

    fun getFileSize(size: Long): String? {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        var value = size.toDouble()
        value = if (value < 1024) {
            return value.toString() + "B"
        } else {
            BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        value = if (value < 1024) {
            return value.toString() + "KB"
        } else {
            BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
        }
        return if (value < 1024) {
            value.toString() + "MB"
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
            value.toString() + "GB"
        }
    }
}