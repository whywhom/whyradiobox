package com.whywhom.soft.whyradiobox.ui.subscription

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase

class SubscriptionViewModel : ViewModel() {
    var podcastlLiveData = MutableLiveData<ArrayList<Podcast>>()

    fun readSubscriptionData(context: Context) {
        Thread {
            var dataBase = PodcastDatabase.getInstance(context)
            var podcastList = dataBase.podcastDao().getAll()
            podcastlLiveData.postValue(ArrayList(podcastList))
        }.start()
    }

    fun getSubscriptionData(): MutableLiveData<ArrayList<Podcast>> {
        return podcastlLiveData
    }

}
