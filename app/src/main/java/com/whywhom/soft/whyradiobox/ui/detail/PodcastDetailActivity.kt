package com.whywhom.soft.whyradiobox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.FeedListAdapter
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSItem
import kotlinx.android.synthetic.main.activity_podcastdetail.*
import kotlinx.android.synthetic.main.content_podcast_detail.*
import kotlinx.android.synthetic.main.main_fragment.swipeRefreshLayout


class PodcastDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: PodcastDetailViewModel
    private lateinit var rssList: ArrayList<RSSItem>
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
        setContentView(R.layout.activity_podcastdetail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        feed_list.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this).get(PodcastDetailViewModel::class.java)
        viewModel.feedUrlLiveData.observe(this, Observer { rssFeed->
            if(rssFeed != null){
                rssList = ArrayList(rssFeed.items)
                var feedListAdapter = FeedListAdapter(this, rssList)
                feed_list.adapter = feedListAdapter
//                swipeRefreshLayout.setRefreshing(false);
            }
        })
//        swipeRefreshLayout.post(Runnable {
//            swipeRefreshLayout.setRefreshing(true)
//            viewModel.getItemFeedUrl(itemInfo)
//        })
        viewModel.getItemFeedUrl(itemInfo)
    }
}
