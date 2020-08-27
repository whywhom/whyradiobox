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

class OnlineSearchViewModel : BaseViewModel() {
    var podcastListLiveData = MutableLiveData<ItunesSearchPodcast>()
    fun itunesPodcastSearcher(searchText: String) {
        launchOnUITryCatch(
            {
                val searchResult = async(Dispatchers.IO) { HttpRepository.search(searchText) }.await()
                podcastListLiveData.postValue(searchResult.await())

            },
            {
                Log.i(HomeViewModel::class.java.simpleName, "${it.message}")
            },
            {
                Log.i(HomeViewModel::class.java.simpleName, "finally")
            },
            true)
    }
}
