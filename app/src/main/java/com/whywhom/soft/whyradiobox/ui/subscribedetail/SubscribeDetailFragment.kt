package com.whywhom.soft.whyradiobox.ui.subscribedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.SubscriptionListAdapter
import com.whywhom.soft.whyradiobox.data.source.local.FeedItem
import com.whywhom.soft.whyradiobox.extensions.generateFileName
import com.whywhom.soft.whyradiobox.extensions.getDiskCacheDir
import com.whywhom.soft.whyradiobox.extensions.getFileNameWithSuffix
import com.whywhom.soft.whyradiobox.interfaces.OnItemClickListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.utils.Constants
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_UNDOWNLOAD
import kotlinx.android.synthetic.main.fragment_subscribedetail.*
import java.text.SimpleDateFormat

class SubscribeDetailFragment : Fragment(), OnItemClickListener, SubscribeDetailViewModel.SubscribeDetailInterface {

    private var feedItemList: ArrayList<FeedItem> = ArrayList<FeedItem>()
    private var showMore:Boolean = false
    private var player: SimpleExoPlayer? = null
    private var isSubscription = false;
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()

    companion object {
        private lateinit var feedUrl: String
        private lateinit var coverUrl: String
        private lateinit var trackId: String
        fun newInstance(url: String, cover: String, trackid: String): SubscribeDetailFragment {
            feedUrl = url
            coverUrl = cover
            trackId = trackid
            return SubscribeDetailFragment()
        }

    }

    private lateinit var viewModel: SubscribeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_subscribedetail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubscribeDetailViewModel::class.java)
        player_control_view.visibility = View.GONE
        feed_list.layoutManager = LinearLayoutManager(this.context)
        viewModel.feedUrlLiveData.observe(viewLifecycleOwner, Observer { rssFeed->
            if(rssFeed != null){
                showDetail(rssFeed!!)
                updateFeedDb()
                showRssList(rssFeed!!)
            }
            swipeRefreshLayout.setRefreshing(false);
        })
        viewModel.feedItemLiveData.observe(viewLifecycleOwner, Observer { feedList->
            if(feedList != null){
                feedItemList = feedList
                var feedListAdapter = SubscriptionListAdapter(this.context!!, feedItemList)
                feedListAdapter.setOnItemClickListener(this)
                feed_list.adapter = feedListAdapter
            }
        })
        swipeRefreshLayout.post(Runnable {
            swipeRefreshLayout.setRefreshing(true)
            viewModel.getItemFeedUrl(feedUrl!!)
        })
        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }

    private fun updateFeedDb() {

    }

    private fun showRssList(rssFeed: RSSFeed) {
        var rssList = ArrayList(rssFeed.items)
        if(feedItemList == null){
            feedItemList = ArrayList<FeedItem>();
        }
        for(i in rssList) {
            var feedItem = FeedItem(0)
            feedItem.downloadUrl = i.enclosure.url.toString()
            feedItem.belongto = generateFileName(feedUrl)
            feedItem.duration = if(i.duration==null)"" else i.duration
            feedItem.length = i.enclosure.length
            feedItem.mediaType = i.enclosure.mimeType
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            feedItem.pubData = formatter.format(i.pubDate)
            feedItem.title = i.title
            feedItem.trackId = trackId
            feedItem.controlType = CONTROL_TYPE_UNDOWNLOAD
            feedItem.filename = getFileNameWithSuffix(feedItem.downloadUrl)
            feedItem.filepath = generateCacheFileName(feedItem.filename)
            feedItemList.add(feedItem)
        }
        viewModel.writeFeedItemToDB(this.context!!,feedItemList,generateFileName(feedUrl));
    }

    private fun generateCacheFileName(feedurl: String): String {
        var path = getDiskCacheDir(this.context!!)
        if(path != null){
            path += "/"+feedurl
        }
        return path
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

        val request: RequestCreator = Picasso.with(context).load(coverUrl).placeholder(R.drawable.rss_64)
        request.fit()
            .centerCrop()
            .into(imgvCover)
    }

    override fun onItemClick(position: Int) {
        var itemData = feedItemList.get(position)
        when(itemData.controlType) {
            CONTROL_TYPE_UNDOWNLOAD->println("UNDOWNLOAD")
            Constants.CONTROL_TYPE_DOWNLOAD ->println("DOWNLOAD")
            Constants.CONTROL_TYPE_DOWNLOADING ->println("DOWNLOADING")
            Constants.CONTROL_TYPE_DOWNLOADED ->println("DOWNLOADED")
            Constants.CONTROL_TYPE_PLAYING ->println("PLAYING")
            Constants.CONTROL_TYPE_PAUSE ->println("PAUSE")
        }
    }

}
