package com.whywhom.soft.whyradiobox.data

import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by wuhaoyong on 2020-01-12.
 */
interface NetworkApiService {
    @GET("search?media=podcast&limit=100")
    fun search(@Query("term") artist: String): Call<ResponseBody>

    @GET("/{lang}/rss/toppodcasts/limit={limit}/explicit=true/json")
    fun getTopList(@Path("lang") user:String, @Path("limit") limit:String): Call<ResponseBody>
}