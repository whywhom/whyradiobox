package com.whywhom.soft.whyradiobox.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.search.OnlineSearchFragment
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : BaseActivity() {

    companion object{
        private var fragmentType: String = ""
        private lateinit var query: String
        private lateinit var podcastTitle: String
        fun newIntent(
            context: Context,
            type: String,
            item: String
        ): Intent {
            query = item
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
                        replace(R.id.container, OnlineSearchFragment.newInstance(query))
                    }
                    else-> {
                        replace(R.id.container, OnlineSearchFragment.newInstance(query))
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
