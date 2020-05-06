package com.whywhom.soft.whyradiobox.ui.subscribedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.whywhom.soft.whyradiobox.R
import kotlinx.android.synthetic.main.app_bar_main_drawer.*

class SubscribeDetailActivity : AppCompatActivity() {

    companion object{
        private lateinit var url: String
        private lateinit var coverurl: String
        private lateinit var trackid: String
        private lateinit var podcastTitle: String
        fun newIntent(
            context: Context?,
            title: String,
            item: String,
            cover: String,
            trackId: String
        ): Intent {
            url = item
            coverurl = cover
            trackid = trackId
            podcastTitle = title
            return Intent(context, SubscribeDetailActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribedetail)
        if(supportActionBar != null) {
            supportActionBar!!.title = podcastTitle
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SubscribeDetailFragment.newInstance(url, coverurl, trackid))
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> {
                this.finish()
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
}
