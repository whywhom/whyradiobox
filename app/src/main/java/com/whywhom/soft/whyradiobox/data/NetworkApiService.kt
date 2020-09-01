package com.whywhom.soft.whyradiobox.data

import com.whywhom.soft.whyradiobox.model.ItunesTopPodcast
import com.whywhom.soft.whyradiobox.model.ItunesSearchPodcast
import kotlinx.coroutines.Deferred
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
    fun getTopList(@Path("lang") user:String, @Path("limit") limit:String): Deferred<ItunesTopPodcast>

}