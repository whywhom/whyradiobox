package com.whywhom.soft.whyradiobox.ui.main

import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.whywhom.soft.whyradiobox.data.NetworkModule
import com.whywhom.soft.whyradiobox.data.source.Podcast
import com.whywhom.soft.whyradiobox.model.ItunesPodcastSearcher
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private var podcastList: ArrayList<Podcast> = ArrayList(0)
    var podcastListLiveData = MutableLiveData<ArrayList<Podcast>>()
    // TODO: Implement the ViewModel
    fun getPodcastList() {
        var network:NetworkModule = NetworkModule()
        network.provideRetrofitService()
            .getTopList("us","20").enqueue(
                object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        var throwable = t
                    }
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>) {
                        var rsp = response.body()!!.string()
                        Log.d("MainViewModel",rsp)
                        var itunesPodcastSearcher: ItunesPodcastSearcher =
                        Gson().fromJson(rsp,ItunesPodcastSearcher::class.java)
                        var list = itunesPodcastSearcher.feed.entry
                    }

                }
            )
    }
}
