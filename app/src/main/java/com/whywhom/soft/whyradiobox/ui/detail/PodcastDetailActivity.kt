package com.whywhom.soft.whyradiobox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.main.OnlineFeedViewFragment


class PodcastDetailActivity : AppCompatActivity() {
    private lateinit var fragment: OnlineFeedViewFragment
    private var coverUrl = ""
    companion object {
        private var itemInfo: String? = ""
        private val INTENT_USER_ID = "user_id"

        fun newIntent(context: Context?, item: String?):Intent {
            itemInfo = item
            return Intent(context, PodcastDetailActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            fragment = OnlineFeedViewFragment.newInstance(this, itemInfo, coverUrl)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}
