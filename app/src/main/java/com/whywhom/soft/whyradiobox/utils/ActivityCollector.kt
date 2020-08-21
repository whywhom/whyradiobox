package com.whywhom.soft.whyradiobox.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by wuhaoyong on 20/08/20.
 */
object ActivityCollector {
    private val activitys = Stack<WeakReference<Activity>>()

    /**
     * Push Activity into the Application stack
     *
     * @param task Activity object to be pushed onto the stack
     */
    fun pushTask(task: WeakReference<Activity>?) {
        activitys.push(task)
    }

    /**
     * Remove the incoming Activity object from the stack
     *
     * @param task
     */
    fun removeTask(task: WeakReference<Activity>?) {
        activitys.remove(task)
    }

    /**
     * Remove Activity from the stack according to the specified position
     *
     * @param taskIndex Activity index
     */
    fun removeTask(taskIndex: Int) {
        if (activitys.size > taskIndex) activitys.removeAt(taskIndex)
    }

    /**
     * Move Activity to the top of the stack
     */
    fun removeToTop() {
        val end = activitys.size
        val start = 1
        for (i in end - 1 downTo start) {
            val mActivity = activitys[i].get()
            if (null != mActivity && !mActivity.isFinishing) {
                mActivity.finish()
            }
        }
    }

    /**
     * Remove all (for the entire application exit)
     */
    fun removeAll() {
        for (task in activitys) {
            val mActivity = task.get()
            if (null != mActivity && !mActivity.isFinishing) {
                mActivity.finish()
            }
        }
    }
}