package com.whywhom.soft.whyradiobox.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryFragment
import com.whywhom.soft.whyradiobox.ui.search.OnlineSearchFragment

class HostActivity : BaseActivity() {

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
                        replace(R.id.container, OnlineSearchFragment.newInstance(action))
                    }
                    "Assets"->{
                        replace(R.id.container, FeedDiscoveryFragment.newInstance(action))
                    }
                    else-> {
                        replace(R.id.container, OnlineSearchFragment.newInstance(action))
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
}
