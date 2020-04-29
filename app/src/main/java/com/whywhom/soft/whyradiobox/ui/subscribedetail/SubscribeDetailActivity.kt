package com.whywhom.soft.whyradiobox.ui.subscribedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whywhom.soft.whyradiobox.R

class SubscribeDetailActivity : AppCompatActivity() {

    companion object{
        private lateinit var url: String
        private lateinit var coverurl: String
        private lateinit var trackid: String
        fun newIntent(
            context: Context?,
            item: String,
            cover: String,
            trackId: String
        ): Intent {
            url = item
            coverurl = cover
            trackid = trackId
            return Intent(context, SubscribeDetailActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribedetail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SubscribeDetailFragment.newInstance(url, coverurl, trackid))
                .commitNow()
        }
    }

}
