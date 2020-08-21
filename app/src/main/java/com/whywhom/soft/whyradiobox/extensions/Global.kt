package com.whywhom.soft.whyradiobox.extensions

import android.content.Context
import android.content.SharedPreferences
import com.whywhom.soft.whyradiobox.RBApplication
import com.whywhom.soft.whyradiobox.utils.Constants

/**
 * Created by wuhaoyong on 20/08/20.
 */

/**
 * 获取SharedPreferences实例。
 */
val sharedPreferences: SharedPreferences = RBApplication.context.getSharedPreferences(Constants.appPackage + "_preferences", Context.MODE_PRIVATE)
