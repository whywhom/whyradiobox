package com.whywhom.soft.whyradiobox.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whywhom.soft.whyradiobox.data.HttpRepository
import com.whywhom.soft.whyradiobox.model.ItunesSearchPodcast
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseViewModel
import com.whywhom.soft.whyradiobox.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.ArrayList

class OnlineSearchViewModel : BaseViewModel() {
    var podcastListLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()
    var itunesSearchPodcast:ItunesSearchPodcast? = null
    fun itunesPodcastSearcher(searchText: String) {
        launchOnUITryCatch(
            {
                val searchResult = async(Dispatchers.IO) { HttpRepository.search(searchText) }.await()
                val itunesSearchPodcast = searchResult.await()
                itunesSearchPodcast?.let {
                    val results: ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
                    for (i in 0 until itunesSearchPodcast.results.size) {
                        var item = itunesSearchPodcast.results.get(i)
                        results.add(PodcastSearchResult(item.trackName,item.artworkUrl100,item.feedUrl,item.artistName))
                    }
                    podcastListLiveData.postValue(results)
                }
            },
            {
                Log.i(OnlineSearchViewModel::class.java.simpleName, "${it.message}")
            },
            {
                Log.i(OnlineSearchViewModel::class.java.simpleName, "finally")
            },
            true)
    }
}
