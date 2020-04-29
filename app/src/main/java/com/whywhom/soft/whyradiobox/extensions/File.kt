package com.whywhom.soft.whyradiobox.extensions

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import org.apache.commons.lang3.ArrayUtils


/**
 * Created by wuhaoyong on 2020-02-13.
 * 获取缓存地址
 */

/**     File paths    **
    Environment.getDataDirectory() = /data
    Environment.getDownloadCacheDirectory() = /cache
    Environment.getExternalStorageDirectory() = /mnt/sdcard
    Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
    Environment.getRootDirectory() = /system
    getPackageCodePath() = /data/app/com.my.app-1.apk
    getPackageResourcePath() = /data/app/com.my.app-1.apk
    getCacheDir() = /data/data/com.my.app/cache
    getDatabasePath(“test”) = /data/data/com.my.app/databases/test
    getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
    getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
    getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
    getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
    getFilesDir() = /data/data/com.my.app/files
*/

val validChars = ("abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "0123456789"
        + " _-").toCharArray()

fun getDiskCacheDir(context: Context): String {
    var cachePath: String? = null
    cachePath = if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()
    ) {
        context.externalCacheDir!!.path
    } else {
        context.cacheDir.path
    }
    return cachePath
}

fun getDiskFileDir(context: Context): String {
    var filePath: String? = null
    filePath = if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()
    ) {
        context.getExternalFilesDir(null)!!.path
    } else {
        context.filesDir.path
    }
    return filePath
}

fun generateFileName(string: String): String {
    val buf = StringBuilder()
    for (i in 0 until string.length) {
        val c = string[i]
        if (Character.isSpaceChar(c)
            && (buf.length == 0 || Character.isSpaceChar(buf[buf.length - 1]))
        ) {
            continue
        }
        if (ArrayUtils.contains(validChars, c)) {
            buf.append(c)
        }
    }
    val filename = buf.toString().trim { it <= ' ' }
    return if (TextUtils.isEmpty(filename)) {
        randomString(8)
    } else filename
}

private fun randomString(length: Int): String {
    val sb = java.lang.StringBuilder(length)
    for (i in 0 until length) {
        sb.append(validChars.get((Math.random() * validChars.size) as Int))
    }
    return sb.toString()
}