package com.whywhom.soft.whyradiobox.extensions

import android.content.SharedPreferences

/**
 * Created by wuhaoyong on 20/08/20.
 */

fun SharedPreferences.extEdit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}