package com.whywhom.soft.whyradiobox.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.whywhom.soft.whyradiobox.data.HttpRepository
import com.whywhom.soft.whyradiobox.data.RBRepository
import com.whywhom.soft.whyradiobox.model.ItunesTopPodcast
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import java.util.*


class HomeViewModel(val repository: RBRepository) : BaseViewModel() {
    var podcastTopCnLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()
    var podcastTopEnLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()

    fun getTopPodcastList(lang: String, rank: String) {
        launchOnUITryCatch(
            {
                val topPodcast = async(IO) { HttpRepository.getTopList(lang, rank) }.await()
                when (lang) {
                    "CN" -> {
                        var itunesTopPodcast: ItunesTopPodcast = topPodcast.await()
                        val results: ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
                        for (i in 0 until itunesTopPodcast.feed.entry.size) {
                            var item = itunesTopPodcast.feed.entry.get(i)
                            results.add(PodcastSearchResult.fromItunesEntry(item))
                        }
                        podcastTopCnLiveData.postValue(results)
                    }
                    "US" -> {
                        var itunesTopPodcast: ItunesTopPodcast = topPodcast.await()
                        val results: ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
                        for (i in 0 until itunesTopPodcast.feed.entry.size) {
                            var item = itunesTopPodcast.feed.entry.get(i)
                            results.add(PodcastSearchResult.fromItunesEntry(item))
                        }
                        podcastTopEnLiveData.postValue(results)
                    }
                }
            },
            {
                Log.i(HomeViewModel::class.java.simpleName, "${it.message}")
            },
            {
                Log.i(HomeViewModel::class.java.simpleName, "finally")
            },
            true
        )
    }


}
