package com.whywhom.soft.whyradiobox.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


/**
 * Created by wuhaoyong on 7/05/20.
 */
object KeyboardUtils {
    fun hideKeyboard(context: Activity) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.window.decorView.windowToken, 0)
    }
}