package com.whywhom.soft.whyradiobox.ui.subscribedetail

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.data.RbFeed
import com.whywhom.soft.whyradiobox.data.source.local.FeedItem
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.rss.RSSReader
import com.whywhom.soft.whyradiobox.utils.RBConfig
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class SubscribeDetailViewModel : ViewModel() {
    private val RSS_UNSUB: Int = 0;
    private val RSS_SUB: Int = 1;
    private var RSS_STATE:Int = RSS_UNSUB;
    private lateinit var feed: RbFeed
    private var podcast = Podcast(0)
    var feedUrlLiveData = MutableLiveData<RSSFeed>()
    var feedItemLiveData = MutableLiveData<ArrayList<FeedItem>>()
    fun getItemFeedUrl(feedUrl: String) {
        var feedUrlLocl = feedUrl
        if (feedUrl.contains("subscribeonandroid.com")) {
            feedUrlLocl = feedUrl.replaceFirst("((www.)?(subscribeonandroid.com/))", "");
        }
        if (!feedUrlLocl!!.contains("itunes.apple.com")) {
            //从搜索的链接获取podcast的真正feedurl
            var disposable = Observable.create(ObservableOnSubscribe<RSSFeed> (){
                if (feedUrl != null) {
                    podcast.url = feedUrl
                    val reader = RSSReader()
                    val rssFeed = reader.load(feedUrl)
                    it.onNext(rssFeed)
                }
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    podcast.title = it.title
                    podcast.description = it.description
//                    podcast.coverurl = podcastSearchResult.imageUrl!!
                    feedUrlLiveData.postValue(it)

                }, {
                    Log.e("PodcastDetailViewModel", it.message.toString())
                })
        } else {
            parseItunesFeedUrl(feedUrl)
        }
    }

    private fun parseItunesFeedUrl(feedUrl: String) {
        var disposable = Observable.create(ObservableOnSubscribe<RSSFeed> (){
            var logging = HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = OkHttpClient.Builder().addInterceptor(logging).build();
            val httpReq = Request.Builder().url(feedUrl)
                .header("User-Agent", RBConfig.USER_AGENT)
            val response = client.newCall(httpReq.build()).execute()
            if (response.isSuccessful) {
                var feedUrl: String = ""
                val resultString = response.body()!!.string()
                feed = Gson().fromJson(resultString, RbFeed::class.java)
                if(feed.results != null) {
                    val results = feed.results[0]
                    feedUrl = results.feedUrl
                    Log.d("feedUrlLiveData", feedUrl)
                    if (feedUrl != null) {
                        val reader = RSSReader()
                        val rssFeed = reader.load(feedUrl)
                        podcast.coverurl = feed.results[0].artworkUrl60!!
                        podcast.rssurl = feed.results[0].feedUrl!!
                        podcast.title = feed.results[0].artistName
                        podcast.trackId = Integer.toString(feed.results[0].trackId)
                        podcast.description = rssFeed.description
                        podcast.url = rssFeed.link.toString();
                        it.onNext(rssFeed)
                    } else{
                        it.onError(NullPointerException())
                    }
                } else{
                    it.onError(NullPointerException())
                }
            } else {
                val prefix: String = "An error occurred:"
                Log.e("PodcastDetailViewModel", prefix + it)
                it.onError(Exception())
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null) {
                    feedUrlLiveData.postValue(it)
                } else {
                    val prefix: String = "An error occurred:"
                    Log.e("PodcastDetailViewModel", prefix + it)
                }
            }, {
                Log.e("PodcastDetailViewModel", it.message.toString())
            })

    }

//    fun subscription(context: Context, rssInterface: SubscribeDetailInterface) {
//        Thread {
//            var dataBase = PodcastDatabase.getInstance(context!!)
//            dataBase.podcastDao().updatePodcast(podcast)
//            rssInterface.onSubscriptionSuccess(true)
//        }.start()
//    }

//    fun getRss(): Podcast {
//        return podcast
//    }

    fun writeFeedItemToDB(
        context: Context,
        feedItemList: ArrayList<FeedItem>,
        generateFileName: String
    ) {
        viewModelScope.launch {
            val withContext = withContext(Dispatchers.IO) {
                var feedItemDao = PodcastDatabase.getInstance(context).FeedItemDao()
                var mutableFeedItems = feedItemDao.loadAllFeedItem(generateFileName)
                for (feed in feedItemList) {
                    var isSame = false;
                    for(feedInDB in mutableFeedItems){
                        if(feed.title == feedInDB.title){
                            isSame = true
                            break
                        }
                    }
                    if(!isSame) {
                        var rowId = feedItemDao.insertFeedItems(feed)
                        Log.d("SubscribeDetail", "rowId = " + rowId)
                    }
                }
                mutableFeedItems = feedItemDao.loadAllFeedItem(generateFileName)
                feedItemLiveData.postValue(ArrayList(mutableFeedItems));
            }
        }
    }

    interface SubscribeDetailInterface{

    }
}
