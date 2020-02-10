package com.whywhom.soft.whyradiobox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.main.RssDetailFragment


class PodcastDetailActivity : AppCompatActivity() {
    private lateinit var fragment: RssDetailFragment

    companion object {
        private lateinit var itemInfo: PodcastSearchResult
        private val INTENT_USER_ID = "user_id"

        fun newIntent(context: Context?, item: PodcastSearchResult):Intent {
            itemInfo = item
            return Intent(context, PodcastDetailActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            fragment = RssDetailFragment.newInstance(this, itemInfo)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}
