package com.whywhom.soft.whyradiobox.ui.discovery

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.model.ItunesSearchPodcast
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.model.SearchResult
import com.whywhom.soft.whyradiobox.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FeedDiscoveryViewModel : BaseViewModel() {
    var podcastCnnLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()
    var podcastBbcLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()
    var podcastVoaLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()

    fun getJsonDataFromAsset(context: Context, media: String) {
        launch(Dispatchers.IO) {
            val jsonString: String
            var gson = Gson()
            try {
                when (media) {
                    "CNN" -> {
                        jsonString =
                            context.assets.open("cnn.json").bufferedReader().use { it.readText() }
                        var itunesSearchPodcast = gson.fromJson(
                            jsonString,
                            ItunesSearchPodcast::class.java
                        )
                        itunesSearchPodcast?.let {
                            val results: java.util.ArrayList<PodcastSearchResult> =
                                java.util.ArrayList<PodcastSearchResult>()
                            for (i in 0 until itunesSearchPodcast.results.size) {
                                var item = itunesSearchPodcast.results.get(i)
                                results.add(PodcastSearchResult(item.trackName,item.artworkUrl100,item.feedUrl,item.artistName))
                            }
                            podcastCnnLiveData.postValue(results)
                        }
                    }
                    "BBC" -> {
                        jsonString =
                            context.assets.open("bbc.json").bufferedReader().use { it.readText() }
                        var itunesSearchPodcast = gson.fromJson(
                            jsonString,
                            ItunesSearchPodcast::class.java
                        )
                        itunesSearchPodcast?.let {
                            val results: java.util.ArrayList<PodcastSearchResult> =
                                java.util.ArrayList<PodcastSearchResult>()
                            for (i in 0 until itunesSearchPodcast.results.size) {
                                var item = itunesSearchPodcast.results.get(i)
                                results.add(PodcastSearchResult(item.trackName,item.artworkUrl100,item.feedUrl,item.artistName))
                            }
                            podcastBbcLiveData.postValue(results)
                        }
                    }
                    "VOA" -> {
                        jsonString =
                            context.assets.open("voa.json").bufferedReader().use { it.readText() }
                        var itunesSearchPodcast = gson.fromJson(
                            jsonString,
                            ItunesSearchPodcast::class.java
                        )
                        itunesSearchPodcast?.let {
                            val results: java.util.ArrayList<PodcastSearchResult> =
                                java.util.ArrayList<PodcastSearchResult>()
                            for (i in 0 until itunesSearchPodcast.results.size) {
                                var item = itunesSearchPodcast.results.get(i)
                                results.add(PodcastSearchResult(item.trackName,item.artworkUrl100,item.feedUrl,item.artistName))
                            }
                            podcastVoaLiveData.postValue(results)
                        }
                    }
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }

    fun itunesPodcastSearcher(searchText: String) {
//        var encodedQuery: String
//        try {
//            encodedQuery = URLEncoder.encode(searchText, "UTF-8")
//        } catch (e: UnsupportedEncodingException) { // this won't ever be thrown
//            encodedQuery = searchText
//        }
//        NetworkModule.provideRetrofitService().search(searchText).enqueue(
//            object : retrofit2.Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    var throwable = t
//                    podcastList.clear()
//                    podcastListLiveData.postValue(podcastList)
//                }
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>) {
//                    if (response.isSuccessful) {
//                        val resultString = response.body()!!.string()
//                        val result = JSONObject(resultString)
//                        val j = result.getJSONArray("results")
//                        podcastList.clear()
//                        for (i in 0 until j.length()) {
//                            val podcastJson = j.getJSONObject(i)
//                            val podcast =
//                                PodcastSearchResult.fromItunes(podcastJson)
//                            podcastList.add(podcast)
//                        }
//                    } else {
//                        podcastList.clear()
//                    }
//                    podcastListLiveData.postValue(podcastList)
//                }
//
//            }
//        )
    }
}
