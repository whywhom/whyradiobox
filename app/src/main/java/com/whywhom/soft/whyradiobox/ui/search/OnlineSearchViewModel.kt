package com.whywhom.soft.whyradiobox.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whywhom.soft.whyradiobox.data.NetworkModule
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import okhttp3.ResponseBody
import org.json.JSONObject

class OnlineSearchViewModel : ViewModel() {
    private var podcastList: ArrayList<PodcastSearchResult> = ArrayList(0)
    var podcastListLiveData = MutableLiveData<ArrayList<PodcastSearchResult>>()
    fun itunesPodcastSearcher(searchText: String) {
//        NetworkModule.provideRetrofitService().search(searchText).enqueue(
//            object : retrofit2.Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    var throwable = t
//                    podcastList.clear()
//                    podcastListLiveData.postValue(podcastList)
//                }
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
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
