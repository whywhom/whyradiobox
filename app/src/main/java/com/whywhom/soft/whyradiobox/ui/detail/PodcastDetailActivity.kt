package com.whywhom.soft.whyradiobox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.Entry
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import kotlinx.android.synthetic.main.activity_podcastdetail.*

class PodcastDetailActivity : AppCompatActivity() {

    companion object {

        private val INTENT_USER_ID = "user_id"
        private lateinit var fragment:PodcastDetailFragment

        fun newIntent(context: Context?, item: PodcastSearchResult): Intent {
            val intent = Intent(context, PodcastDetailActivity::class.java)
            fragment = PodcastDetailFragment.newInstance(item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcastdetail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
