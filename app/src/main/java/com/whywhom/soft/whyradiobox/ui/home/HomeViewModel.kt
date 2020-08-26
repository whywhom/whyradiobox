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
    var podcastCnnLiveData = MutableLiveData<ItunesSearchPodcast>()
    var podcastBbcLiveData = MutableLiveData<ItunesSearchPodcast>()
    var podcastVoaLiveData = MutableLiveData<ItunesSearchPodcast>()
//
//    fun itunesPodcastSearcher(searchText: String) {
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
//    }

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

    fun getJsonDataFromAsset(context: Context, media: String) {
        launch(IO) {
            val jsonString: String
            var gson = Gson()
            try {
                when (media) {
                    "CNN" -> {
                        jsonString =
                            context.assets.open("cnn.json").bufferedReader().use { it.readText() }
                        podcastCnnLiveData.postValue(
                            gson.fromJson(
                                jsonString,
                                ItunesSearchPodcast::class.java
                            )
                        )
                    }
                    "BBC" -> {
                        jsonString =
                            context.assets.open("bbc.json").bufferedReader().use { it.readText() }
                        podcastBbcLiveData.postValue(
                            gson.fromJson(
                                jsonString,
                                ItunesSearchPodcast::class.java
                            )
                        )
                    }
                    "VOA" -> {
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
}
