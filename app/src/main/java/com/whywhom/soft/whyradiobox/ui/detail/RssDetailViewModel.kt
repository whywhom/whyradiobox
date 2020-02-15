package com.whywhom.soft.whyradiobox.ui.detail

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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import java.io.IOException


class RssDetailViewModel : ViewModel() {
    private lateinit var feed: RbFeed
    private var podcast = Podcast()
    var feedUrlLiveData = MutableLiveData<RSSFeed>()

    fun getItemFeedUrl(podcastSearchResult: PodcastSearchResult) {
        if (!podcastSearchResult.feedUrl!!.contains("itunes.apple.com")) {
            object : Thread() {
                override fun run() {
                    try {
                        if (podcastSearchResult.feedUrl != null) {
                            podcast.url = podcastSearchResult.feedUrl
                            val reader = RSSReader()
                            val rssFeed = reader.load(podcastSearchResult.feedUrl)
                            podcast.title = rssFeed.title
                            podcast.description = rssFeed.description
                            podcast.coverurl = podcastSearchResult.imageUrl!!
                            feedUrlLiveData.postValue(rssFeed)
                        }
                    } catch (e: IOException) {
                        Log.e("PodcastDetailViewModel", e.message.toString())
                    } catch (e: JSONException) {
                        Log.e("PodcastDetailViewModel", e.message.toString())
                    }
                }
            }.start()
        } else {
            parseFeedUrl(podcastSearchResult)
        }
    }

    private fun parseFeedUrl(podcastSearchResult: PodcastSearchResult) {
        object : Thread() {
            override fun run() {
                var logging = HttpLoggingInterceptor();
                var feedUrl: String = ""
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                var client = OkHttpClient.Builder().addInterceptor(logging).build();
                val httpReq = Request.Builder()
                    .url(podcastSearchResult.feedUrl)
                    .header("User-Agent", RBConfig.USER_AGENT)
                try {
                    val response = client.newCall(httpReq.build()).execute()
                    if (response.isSuccessful) {
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
                                feedUrlLiveData.postValue(rssFeed)
                            }
                        }
                    } else {
                        val prefix: String = "An error occurred:"
                        Log.e("PodcastDetailViewModel", prefix + response)
                    }
                } catch (e: IOException) {
                    Log.e("PodcastDetailViewModel", e.message.toString())
                } catch (e: JSONException) {
                    Log.e("PodcastDetailViewModel", e.message.toString())
                }
            }
        }.start()
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
