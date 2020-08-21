package com.whywhom.soft.whyradiobox.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.whywhom.soft.whyradiobox.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by wuhaoyong on 21/08/20.
 */
open class BaseFragment: Fragment() {
    /**
     * 是否已经加载过数据
     */
    private var mHasLoadedData = false

    /**
     * Fragment中由于服务器或网络异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null

    /**
     * Fragment中显示加载等待的控件。
     */
    protected var loading: ProgressBar? = null

    /**
     * 依附的Activity
     */
    lateinit var activity: Activity

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 缓存当前依附的activity
        activity = getActivity()!!
        Log.d(TAG, "BaseFragment-->onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BaseFragment-->onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "BaseFragment-->onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "BaseFragment-->onViewCreated()")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "BaseFragment-->onActivityCreated()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "BaseFragment-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "BaseFragment-->onResume()")
        //当Fragment在屏幕上可见并且没有加载过数据时调用
        if (!mHasLoadedData) {
            loadDataOnce()
            Log.d(TAG, "BaseFragment-->loadDataOnce()")
            mHasLoadedData = true
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "BaseFragment-->onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "BaseFragment-->onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "BaseFragment-->onDestroyView()")
        EventBus.getDefault().unregister(this)
        if (rootView?.parent != null) (rootView?.parent as ViewGroup).removeView(rootView)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "BaseFragment-->onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "BaseFragment-->onDetach()")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        Log.d(TAG, "BaseFragment-->onMessageEvent()")
    }

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    fun onCreateView(view: View): View {
        Log.d(TAG, "BaseFragment-->onCreateView()")
        rootView = view
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        return view
    }

    /**
     * 页面首次可见时调用一次该方法，在这里可以请求网络数据等。
     */
    open fun loadDataOnce() {

    }

}