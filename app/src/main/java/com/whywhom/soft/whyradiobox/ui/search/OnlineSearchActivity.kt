package com.whywhom.soft.whyradiobox.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.SearchEvent
import androidx.fragment.app.Fragment
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.event.FragmentEvent
import com.whywhom.soft.whyradiobox.event.MessageEvent
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.model.SearchResult
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.detail.OnlineFeedViewActivity
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryFragment
import com.whywhom.soft.whyradiobox.ui.search.OnlineSearchFragment

class OnlineSearchActivity : BaseActivity() {
    lateinit var currentFragment: Fragment
    companion object{
        private var fragmentType: String = ""
        private lateinit var action: String
        private lateinit var podcastTitle: String
        fun newIntent(
            context: Context,
            type: String,
            item: String
        ): Intent {
            action = item
            fragmentType = type
            return Intent(context, OnlineSearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                when(fragmentType) {
                    "Search"-> {
                        currentFragment = OnlineSearchFragment.newInstance(action)
                        replace(R.id.container, currentFragment)
                    }
                    else-> {
                        currentFragment = OnlineSearchFragment.newInstance(action)
                        replace(R.id.container, currentFragment)
                    }
                }
                commitNow()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                this.finish()
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is FragmentEvent && this::class.java == messageEvent.activityClass) {
            showRssDetail(messageEvent.entry)
        }
    }

    fun showRssDetail(entry: PodcastSearchResult) {
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.container, OnlineFeedViewFragment.newInstance(this@HostActivity,feedUrl!!, artworkUrl100!!))
//            addToBackStack(null)
//        }.commitAllowingStateLoss()
        val intent = OnlineFeedViewActivity.newIntent(this, entry)
        startActivity(intent)
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
