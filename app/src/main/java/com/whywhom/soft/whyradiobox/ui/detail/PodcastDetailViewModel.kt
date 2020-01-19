package com.whywhom.soft.whyradiobox.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.utils.RBConfig
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class PodcastDetailViewModel : ViewModel(){

    fun getItemFeedUrl(info: PodcastSearchResult) {
        var result = getFeedUrl(info)
        if(!result.isEmpty()){

        }
    }

    fun getFeedUrl(podcast: PodcastSearchResult):String {
        if (!podcast.feedUrl!!.contains("itunes.apple.com")) {
            return podcast.feedUrl
        } else {
            var logging = HttpLoggingInterceptor();
            var feedUrl:String = ""
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = OkHttpClient.Builder().addInterceptor(logging).build();
            val httpReq = Request.Builder()
                .url(podcast.feedUrl)
                .header("User-Agent", RBConfig.USER_AGENT)
            try {
                val response = client.newCall(httpReq.build()).execute()
                if (response.isSuccessful) {
                    val resultString = response.body()!!.string()
                    val result = JSONObject(resultString)
                    val results = result.getJSONArray("results").getJSONObject(0)
                    feedUrl = results.getString("feedUrl")
                } else {
                    val prefix: String = "An error occurred:"
                    Log.e("PodcastDetailViewModel", prefix+response)
                    feedUrl = ""
                }
            } catch (e: IOException) {
                feedUrl = ""
            } catch (e: JSONException) {
                feedUrl = ""
            } finally {
                return feedUrl
            }
        }
    }
}
