package com.whywhom.soft.whyradiobox.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.data.HttpRepository
import com.whywhom.soft.whyradiobox.data.RBRepository
import com.whywhom.soft.whyradiobox.model.ItunesPodcastSearcher
import com.whywhom.soft.whyradiobox.model.ItunesSearchPodcast
import com.whywhom.soft.whyradiobox.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException


class HomeViewModel(val repository: RBRepository) : BaseViewModel() {
    var podcastTopCnLiveData = MutableLiveData<ItunesPodcastSearcher>()
    var podcastTopEnLiveData = MutableLiveData<ItunesPodcastSearcher>()

    fun getTopPodcastList(lang: String, rank: String) {
        launchOnUITryCatch(
            {
                val topPodcast = async(IO) { HttpRepository.getTopList(lang, rank) }.await()
                when(lang) {
                    "CN" -> {
                        podcastTopCnLiveData.postValue(topPodcast.await())
                    }
                    "US" -> {
                        podcastTopEnLiveData.postValue(topPodcast.await())
                    }
                }
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
