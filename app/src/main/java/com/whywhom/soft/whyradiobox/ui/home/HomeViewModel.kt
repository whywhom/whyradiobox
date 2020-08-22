package com.whywhom.soft.whyradiobox.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.data.NetworkModule
import com.whywhom.soft.whyradiobox.data.RBRepository
import com.whywhom.soft.whyradiobox.model.ItunesPodcastSearcher
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.home.HomeFragment.Companion.MAX_TOPLIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel(val repository: RBRepository) : ViewModel() {
    private var podcastList: ArrayList<PodcastSearchResult> = ArrayList(0)
    var podcastListLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()

    fun getTopPodcastList() {
        var country: String = Locale.getDefault().getCountry().decapitalize()
        podcastList.clear()
        NetworkModule.provideRetrofitService()
            .getTopList(country, MAX_TOPLIST.toString()).enqueue(
                object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        var throwable = t
                        podcastList.clear()
                        podcastListLiveData.postValue(podcastList)
                    }
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>) {
                        var rsp = response.body()!!.string()
                        val result = JSONObject(rsp)
                        val feed: JSONObject = result.getJSONObject("feed")
                        val entries = feed.getJSONArray("entry")
                        var itunesPodcastSearcher: ItunesPodcastSearcher =
                        Gson().fromJson(rsp,ItunesPodcastSearcher::class.java)
                        var i = 0
                        for (i in 0 until entries.length()) {
                            val json = entries.getJSONObject(i)
                            podcastList.add(PodcastSearchResult.fromItunesToplist(json));
                        }
                        podcastListLiveData.postValue(podcastList)
                    }

                }
            )
    }

    fun itunesPodcastSearcher(searchText: String) {
        NetworkModule.provideRetrofitService().search(searchText).enqueue(
            object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    var throwable = t
                    podcastList.clear()
                    podcastListLiveData.postValue(podcastList)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val resultString = response.body()!!.string()
                        val result = JSONObject(resultString)
                        val j = result.getJSONArray("results")
                        podcastList.clear()
                        for (i in 0 until j.length()) {
                            val podcastJson = j.getJSONObject(i)
                            val podcast =
                                PodcastSearchResult.fromItunes(podcastJson)
                            podcastList.add(podcast)
                        }
                    } else {
                        podcastList.clear()
                    }
                    podcastListLiveData.postValue(podcastList)
                }

            }
        )
    }

    fun getTopPodcastList(lang: String, rank: String) {
        podcastList.clear()
        runBlocking<Unit> {
            val response = repository.refreshToprank(lang, rank)
            val resultString = response!!.string()

        }
    }
}
