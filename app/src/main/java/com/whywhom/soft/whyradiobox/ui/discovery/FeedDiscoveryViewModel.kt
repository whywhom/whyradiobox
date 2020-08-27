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
    private var podcastList: ArrayList<SearchResult> = ArrayList(0)
    var podcastListLiveData = MutableLiveData<ArrayList<SearchResult>>()
    var podcastCnnLiveData = MutableLiveData<ItunesSearchPodcast>()
    var podcastBbcLiveData = MutableLiveData<ItunesSearchPodcast>()
    var podcastVoaLiveData = MutableLiveData<ItunesSearchPodcast>()

    fun getJsonDataFromAsset(context: Context, media: String) {
        launch(Dispatchers.IO) {
            val jsonString: String
            var gson = Gson()
            try {
                when (media) {
                    "Assets:CNN" -> {
                        jsonString =
                            context.assets.open("cnn.json").bufferedReader().use { it.readText() }
                        podcastCnnLiveData.postValue(
                            gson.fromJson(
                                jsonString,
                                ItunesSearchPodcast::class.java
                            )
                        )
                    }
                    "Assets:BBC" -> {
                        jsonString =
                            context.assets.open("bbc.json").bufferedReader().use { it.readText() }
                        podcastBbcLiveData.postValue(
                            gson.fromJson(
                                jsonString,
                                ItunesSearchPodcast::class.java
                            )
                        )
                    }
                    "Assets:VOA" -> {
                        jsonString =
                            context.assets.open("voa.json").bufferedReader().use { it.readText() }
                        podcastVoaLiveData.postValue(
                            gson.fromJson(
                                jsonString,
                                ItunesSearchPodcast::class.java
                            )
                        )
                    }
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }

    fun getTopPodcastList(searchType: Int) {
        var country: String = Locale.getDefault().getCountry().decapitalize().toUpperCase()
//        if(searchType == FeedDiscoveryFragment.TYPE_EN){
//            country = "US"
//        }
//        if(searchType == FeedDiscoveryFragment.TYPE_CN){
//            country = "CN"
//        }
        podcastList.clear()
//        NetworkModule.provideRetrofitService()
//            .getTopList(country,"50").enqueue(
//                object : retrofit2.Callback<ResponseBody> {
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        var throwable = t
//                        podcastList.clear()
//                        podcastListLiveData.postValue(podcastList)
//                    }
//                    override fun onResponse(
//                        call: Call<ResponseBody>,
//                        response: Response<ResponseBody>) {
//                        var rsp = response.body()!!.string()
//                        val result = JSONObject(rsp)
//                        val feed: JSONObject = result.getJSONObject("feed")
//                        val entries = feed.getJSONArray("entry")
//                        var itunesPodcastSearcher: ItunesPodcastSearcher =
//                        Gson().fromJson(rsp,ItunesPodcastSearcher::class.java)
//                        var i = 0
//                        for (i in 0 until entries.length()) {
//                            val json = entries.getJSONObject(i)
//                            podcastList.add(PodcastSearchResult.fromItunesToplist(json));
//                        }
//                        podcastListLiveData.postValue(podcastList)
//                    }
//
//                }
//            )
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
