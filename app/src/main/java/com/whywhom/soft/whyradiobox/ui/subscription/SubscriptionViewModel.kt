package com.whywhom.soft.whyradiobox.ui.subscription

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDao
import com.whywhom.soft.whyradiobox.data.source.local.PodcastDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscriptionViewModel : ViewModel() {
    var podcastlLiveData = MutableLiveData<ArrayList<Podcast>>()

    fun readSubscriptionData(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var dataBase = PodcastDatabase.getInstance(context)
                var podcastList = dataBase.podcastDao().getAll()
                podcastlLiveData.postValue(ArrayList(podcastList))
            }
        }
    }

    fun getSubscriptionData(): MutableLiveData<ArrayList<Podcast>> {
        return podcastlLiveData
    }

}
