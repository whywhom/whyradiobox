package com.whywhom.soft.whyradiobox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.main.OnlineFeedViewFragment


class OnlineFeedViewActivity : BaseActivity() {
    private lateinit var fragment: OnlineFeedViewFragment
    companion object {
        private var feed: String? = ""
        private var cover: String? = ""
        private var name: String? = ""
        fun newIntent(context: Context?, entry: PodcastSearchResult):Intent {
            name = entry.title
            feed = entry.feedUrl
            cover = entry.imageUrl
            return Intent(context, OnlineFeedViewActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_feed)
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//显示返回图标
        supportActionBar?.setHomeButtonEnabled(true);//主键按钮能否可点击
//        actionBar?.title = name
//        actionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            fragment = OnlineFeedViewFragment.newInstance(this, feed, cover)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
