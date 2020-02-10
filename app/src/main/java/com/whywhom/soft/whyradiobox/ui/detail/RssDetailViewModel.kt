package com.whywhom.soft.whyradiobox.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.rss.RSSFeed
import com.whywhom.soft.whyradiobox.rss.RSSItem
import com.whywhom.soft.whyradiobox.rss.RSSReader
import com.whywhom.soft.whyradiobox.utils.RBConfig
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class RssDetailViewModel : ViewModel() {

    var feedUrlLiveData = MutableLiveData<RSSFeed>()
    fun getItemFeedUrl(podcast: PodcastSearchResult) {
        if (!podcast.feedUrl!!.contains("itunes.apple.com")) {
            object : Thread() {
                override fun run() {
                    try {
                        if (podcast.feedUrl != null) {
                            val reader = RSSReader()
                            val rssFeed = reader.load(podcast.feedUrl)
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
            parseFeedUrl(podcast)
        }
    }

    private fun parseFeedUrl(podcast: PodcastSearchResult) {
        object : Thread() {
            override fun run() {
                var logging = HttpLoggingInterceptor();
                var feedUrl: String = ""
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                var client = OkHttpClient.Builder().addInterceptor(logging).build();
                val httpReq = Request.Builder()
                    .url(podcast.feedUrl)
                    .header("User-Agent", RBConfig.USER_AGENT)
                try {
                    val response = client.newCall(httpReq.build()).execute()
                    if (response.isSuccessful) {
                        val resultString = response.body()!!.string()
                        val result = JSONObject(resultString)
                        val results = result.getJSONArray("results").getJSONObject(0)
                        feedUrl = results.getString("feedUrl")
                        Log.d("feedUrlLiveData", feedUrl)
                        if (feedUrl != null) {
                            val reader = RSSReader()
                            val rssFeed = reader.load(feedUrl)
                            feedUrlLiveData.postValue(rssFeed)
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
}
