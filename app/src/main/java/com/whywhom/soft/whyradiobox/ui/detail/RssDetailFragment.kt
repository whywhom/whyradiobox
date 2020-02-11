package com.whywhom.soft.whyradiobox.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.FeedListAdapter
import com.whywhom.soft.whyradiobox.interfaces.OnPlayListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.rss.RSSItem
import com.whywhom.soft.whyradiobox.ui.detail.RssDetailViewModel
import com.whywhom.soft.whyradiobox.utils.PlayerUtil
import kotlinx.android.synthetic.main.fragment_rss_detail.*

class RssDetailFragment : Fragment(), OnPlayListener {
    private lateinit var rssList: ArrayList<RSSItem>
    private var showMore:Boolean = false
    private var player:SimpleExoPlayer? = null
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        private lateinit var itemInfo: PodcastSearchResult

        fun newInstance(context: Context?, item: PodcastSearchResult):RssDetailFragment{
            itemInfo = item
            return RssDetailFragment()
        }
    }

    private lateinit var viewModel: RssDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_rss_detail, container, false)
        return view
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        PlayerUtil.resetPlayer()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        player_control_view.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RssDetailViewModel::class.java)
        feed_list.layoutManager = LinearLayoutManager(this.context)
        viewModel = ViewModelProviders.of(this).get(RssDetailViewModel::class.java)
        viewModel.feedUrlLiveData.observe(this, Observer { rssFeed->
            if(rssFeed != null){
                showDetail(rssFeed!!)
                showRssList(rssFeed!!)
            }
            swipeRefreshLayout.setRefreshing(false);
        })
        swipeRefreshLayout.post(Runnable {
            swipeRefreshLayout.setRefreshing(true)
            viewModel.getItemFeedUrl(itemInfo)
        })
        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }

    private fun showRssList(rssFeed: RSSFeed) {
        rssList = ArrayList(rssFeed.items)
        var feedListAdapter = FeedListAdapter(this.context!!, rssList)
        feedListAdapter.setOnPlayListener(this)
        feed_list.adapter = feedListAdapter
    }

    private fun showDetail(rssFeed: RSSFeed) {
        rss_detail.visibility = View.VISIBLE
        tv_title.text = rssFeed.title
        tv_author.text = rssFeed.author
        tv_description.text = rssFeed.description
        tv_show_more.setOnClickListener { click->
            showMore = !showMore
            if(!showMore){
                tv_description.visibility = View.GONE
                tv_show_more.text = getString(R.string.show_more)
            } else{
                tv_description.visibility = View.VISIBLE
                tv_show_more.text = getString(R.string.show_less)
            }
        }
        val request: RequestCreator =Picasso.with(context).load(itemInfo.imageUrl).placeholder(R.drawable.rss_64)
        request.fit()
            .centerCrop()
            .into(imgvCover)
    }

    override fun play(position: Int) {
        var mineType:String = ""
        lateinit var uri:Uri
        player = PlayerUtil.getPlayer(this.context!!)
        if(!player_control_view.isVisible){
            player_control_view.visibility = View.VISIBLE
        }
        if(player!!.isPlaying){
            player!!.stop()
        }
        // Produces DataSource instances through which media data is loaded.
        if(rssList[position].enclosure != null){
            mineType = rssList[position].enclosure.mimeType
            uri = rssList[position].enclosure.url
        } else{
            mineType = Util.getUserAgent(context, "RBApplication")
            if(rssList[position].link != null){
                uri = rssList[position].link
            }
        }

        var dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this.context, mineType)
        // This is the MediaSource representing the media to be played.
        var videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
        // Prepare the player with the source.
        player!!.playWhenReady = true
        player_control_view.player = player
//        player_control_view.showTimeoutMs = -1
        player!!.prepare(videoSource);
    }
}
