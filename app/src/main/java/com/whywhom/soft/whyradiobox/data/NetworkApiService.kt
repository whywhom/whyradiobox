package com.whywhom.soft.whyradiobox.data

import com.whywhom.soft.whyradiobox.model.ItunesPodcastSearcher
import com.whywhom.soft.whyradiobox.model.ItunesSearchPodcast
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by wuhaoyong on 2020-01-12.
 */
interface NetworkApiService {
    @GET("search?media=podcast")
    fun search(@Query("term") artist: String): Deferred<ItunesSearchPodcast>

    @GET("/{lang}/rss/toppodcasts/limit={limit}/explicit=true/json")
    fun getTopList(@Path("lang") user:String, @Path("limit") limit:String): Deferred<ItunesPodcastSearcher>

}