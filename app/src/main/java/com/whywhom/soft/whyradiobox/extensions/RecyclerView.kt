package com.whywhom.soft.whyradiobox.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.whywhom.soft.whyradiobox.interfaces.RecyclerListener

/**
 * Created by wuhaoyong on 2020-01-08.
 */

fun RecyclerView.setListener(l: RecyclerListener){
    setOnScrollListener(object : RecyclerView.OnScrollListener() {
        var lastVisibleItem: Int = 0
        val swipeRefreshLayout = this@setListener.parent

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 === recyclerView.adapter?.itemCount) {
                //下拉刷新的时候不可以加载更多
                if(swipeRefreshLayout is SwipeRefreshLayout){
                    if(!swipeRefreshLayout.isRefreshing){
                        l.loadMore()
                    }
                }else{
                    l.loadMore()
                }
            }
        }

    })

    val swipeRefreshLayout = this.parent
    if(swipeRefreshLayout is SwipeRefreshLayout){
        swipeRefreshLayout.setOnRefreshListener {
            l.refresh()
        }
    }

}