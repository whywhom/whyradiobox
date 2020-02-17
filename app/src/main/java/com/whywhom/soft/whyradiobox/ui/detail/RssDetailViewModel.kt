package com.whywhom.soft.whyradiobox.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.data.RbFeed
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.rss.RSSReader
import com.whywhom.soft.whyradiobox.utils.RBConfig
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import java.io.IOException


class RssDetailViewModel : ViewModel() {
    private lateinit var feed: RbFeed
    private var podcast = Podcast()
    var feedUrlLiveData = MutableLiveData<RSSFeed>()

    fun getItemFeedUrl(podcastSearchResult: PodcastSearchResult) {
        if (!podcastSearchResult.feedUrl!!.contains("itunes.apple.com")) {
            var disposable = Observable.create(ObservableOnSubscribe<RSSFeed> (){
                if (podcastSearchResult.feedUrl != null) {
                    podcast.url = podcastSearchResult.feedUrl
                    val reader = RSSReader()
                    val rssFeed = reader.load(podcastSearchResult.feedUrl)
                    it.onNext(rssFeed)
                }
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    podcast.title = it.title
                    podcast.description = it.description
                    podcast.coverurl = podcastSearchResult.imageUrl!!
                    feedUrlLiveData.postValue(it)

                }, {
                    Log.e("PodcastDetailViewModel", it.message.toString())
                })
        } else {
            parseFeedUrl(podcastSearchResult)
        }
    }

    private fun parseFeedUrl(podcastSearchResult: PodcastSearchResult) {

        var disposable = Observable.create(ObservableOnSubscribe<RSSFeed> (){
            var logging = HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = OkHttpClient.Builder().addInterceptor(logging).build();
            val httpReq = Request.Builder()
                .url(podcastSearchResult.feedUrl)
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
                        podcast.url = feed.results[0].feedUrl!!
                        podcast.title = feed.results[0].artistName
                        podcast.id = Integer.toString(feed.results[0].trackId)
                        podcast.description = rssFeed.description
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
        }).subscribeOn(Schedulers.io())
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

    fun readPodcastFromDB() {
        Thread {
//            podcastDbList = DbData.podcastDbList
//            if (podcastDbList != null) {
//                var count = podcastDbList.size
////                var itemList:ArrayList<Podcast> = ArrayList(0)
//                podcastList.clear()
//                for(podcast in podcastDbList){
//                    var podcastItem:Podcast = Podcast.fromDb(podcast)
//                    podcastList.add(podcastItem)
//                }
//                podcastLists.postValue(podcastList)
//            }
        }.start()
    }

    fun subscription(context: Context) {
        Thread {
            var dataBase = PodcastDatabase.getInstance(context!!)
            dataBase.podcastDao().updatePodcast(podcast)
        }.start()
    }
}
