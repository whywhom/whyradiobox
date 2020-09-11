package com.whywhom.soft.whyradiobox.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.FeedListAdapter
import com.whywhom.soft.whyradiobox.interfaces.OnPlayListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.rss.RSSItem
import com.whywhom.soft.whyradiobox.services.MediaPlayService
import com.whywhom.soft.whyradiobox.ui.BaseFragment
import com.whywhom.soft.whyradiobox.ui.detail.OnlineFeedViewModel
import com.whywhom.soft.whyradiobox.ui.subscribedetail.SubscribeDetailActivity
import com.whywhom.soft.whyradiobox.utils.PlayerUtil
import kotlinx.android.synthetic.main.fragment_rss_detail.*


class OnlineFeedViewFragment : BaseFragment(), OnPlayListener, OnlineFeedViewModel.RssInterface {

    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val PLAYBACK_CHANNEL_ID = "playback_channel"
    private val PLAYBACK_NOTIFICATION_ID = 1

    private lateinit var rssList: ArrayList<RSSItem>
    private var showMore:Boolean = false
    private var player:SimpleExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    private var isSubscription = false;
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()


    companion object {
        private var feedUrl: String? = ""
        private var coverUrl:String? =""
        private var title:String = ""
        fun newInstance(context: Context?, item: String?, cover: String?):OnlineFeedViewFragment{
            feedUrl = item
            coverUrl = cover
            return OnlineFeedViewFragment()
        }
    }

    private lateinit var viewModel: OnlineFeedViewModel

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
        val bundle = savedInstanceState ?: arguments
        if(bundle != null){
            title = bundle.getString("title","")
            feedUrl = bundle.getString("feed_url","")
            coverUrl = bundle.getString("feed_cover_url","")
        }
    }

    private fun checkSubscription(rssFeed: RSSFeed) {
        viewModel.checkSubscription(this.requireContext(),rssFeed, this);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(feedUrl == null){
//            findNavController().popBackStack();
        }

        viewModel = ViewModelProvider(this).get(OnlineFeedViewModel::class.java)
        feed_list.layoutManager = LinearLayoutManager(this.context)
        viewModel.feedUrlLiveData.observe(viewLifecycleOwner, Observer { rssFeed->
            if(rssFeed != null){
                checkSubscription(rssFeed)
                showDetail(rssFeed!!)
                showRssList(rssFeed!!)
            }
            swipeRefreshLayout.isRefreshing = false;
        })
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            viewModel.getItemFeedUrl(feedUrl!!, coverUrl)
        }
        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }

    private fun showRssList(rssFeed: RSSFeed) {
        rssList = ArrayList(rssFeed.items)
        var feedListAdapter = FeedListAdapter(this.requireContext(), rssList)
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

        btn_subscribe.setOnClickListener{
            if(isSubscription){
                val data = viewModel.getRss()
                val intent = SubscribeDetailActivity.newIntent(this.context, data.title, data.rssurl, data.coverurl, data.trackId)
                startActivity(intent)
            } else {
                viewModel.subscription(this.requireContext(), this)
            }
        }

        val request: RequestCreator =Picasso.get().load(coverUrl).placeholder(R.drawable.rss_96)
        request.fit()
            .centerCrop()
            .into(imgvCover)
    }

    override fun play(position: Int) {
        var mineType:String = ""
        lateinit var uri:Uri
        var rssItem = rssList[position]
//        player = PlayerUtil.getPlayer(this.requireContext())
//
//        if(!player_control_view.isVisible){
//            player_control_view.visibility = View.VISIBLE
//        }
//        if(player!!.isPlaying){
//            player!!.stop()
//        }
//        // Produces DataSource instances through which media data is loaded.
//        if(rssItem.enclosure != null){
//            mineType = rssItem.enclosure.mimeType
//            uri = rssItem.enclosure.url
//        } else{
//            mineType = Util.getUserAgent(context!!, "RBApplication")
//            if(rssItem.link != null){
//                uri = rssItem.link
//            }
//        }
//        var dataSourceFactory: DataSource.Factory =
//            DefaultDataSourceFactory(this.context, mineType)
//        // This is the MediaSource representing the media to be played.
//        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(uri)
//        // Prepare the player with the source.
//        player!!.playWhenReady = true
//        player_control_view.player = player
////        player_control_view.showTimeoutMs = -1
//        player!!.prepare(mediaSource);
//        val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
//            override fun getCurrentSubText(player: Player): String? {
//                return rssItem.description
//            }
//
//            override fun getCurrentContentTitle(player: Player): String {
//                return rssItem.title
//            }
//
//            override fun createCurrentContentIntent(player: Player): PendingIntent? {
//                return null
//            }
//
//            override fun getCurrentContentText(player: Player): String {
//                if(rssItem.getAuthor() != null){
//                    return rssItem.getAuthor()
//                }
//                return ""
//            }
//
//            override fun getCurrentLargeIcon(
//                player: Player,
//                callback: PlayerNotificationManager.BitmapCallback
//            ): Bitmap? {
//                return null
//            }
//        }
//        this.context?.let { PlayerUtil.initListener(it,mediaDescriptionAdapter) }
        val intent = Intent(this.context, MediaPlayService::class.java)
        MediaPlayService.setDataSource(rssItem)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            activity.startForegroundService(intent)
        } else{
            activity.startService(intent)
        }
    }

    override fun pause(position: Int) {

    }
    private fun updateBtn(it: Boolean) {
        isSubscription = it;
        if (it) {
            btn_subscribe.text = getString(R.string.btn_view)
        } else {
            btn_subscribe.text = getString(R.string.subscribe)
        }
    }

    override fun isSubscripted(it:Boolean) {
        updateBtn(it)
    }

    override fun onSubscriptionSuccess(it: Boolean) {
        updateBtn(it)
    }
}
