package com.whywhom.soft.whyradiobox.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.event.FragmentEvent
import com.whywhom.soft.whyradiobox.event.MessageEvent
import com.whywhom.soft.whyradiobox.event.RefreshEvent
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryFragment
import com.whywhom.soft.whyradiobox.ui.search.OnlineSearchFragment

class HostActivity : BaseActivity() {
    lateinit var currentFragment: Fragment
    lateinit var preFragment: Fragment
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
            return Intent(context, HostActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                when(fragmentType) {
                    "Search"-> {
                        currentFragment = OnlineSearchFragment.newInstance(action)
                        replace(R.id.container, currentFragment)
                    }
                    "Assets"->{
                        currentFragment = FeedDiscoveryFragment.newInstance(action)
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
            Log.d(TAG, messageEvent.toString())
        }
    }

    fun showDetailFragment(feedUrl: String, artworkUrl100: String) {
        supportFragmentManager.beginTransaction().apply {

            preFragment = currentFragment
            if(preFragment != null) {
                hide(preFragment)
            }
            currentFragment = OnlineFeedViewFragment.newInstance(this@HostActivity,feedUrl!!, artworkUrl100!!)
            add(R.id.container, currentFragment, currentFragment::class.java.simpleName)
//            show(currentFragment)
//            replace(R.id.container, OnlineFeedViewFragment.newInstance(this@HostActivity, feedUrl!!, artworkUrl100!!))
            addToBackStack(currentFragment::class.java.simpleName)
        }.commitAllowingStateLoss()
    }
}
