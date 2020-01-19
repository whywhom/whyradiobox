package com.whywhom.soft.whyradiobox.model

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by wuhaoyong on 2020-01-15.
 */

data class PodcastSearchResult (
    /**
     * The name of the podcast
     */
    val title: String? = null,
    /**
     * URL of the podcast image
     */
    val imageUrl: String? = null,
    /**
     * URL of the podcast feed
     */
    val feedUrl: String? = null
){

    companion object {
        fun dummy(): PodcastSearchResult? {
            return PodcastSearchResult("", "", "")
        }

        /**
         * Constructs a Podcast instance from a iTunes search result
         *
         * @param json object holding the podcast information
         * @throws JSONException
         */
        fun fromItunes(json: JSONObject): PodcastSearchResult {
            val title = json.optString("collectionName", "")
            val imageUrl = json.optString("artworkUrl100", null)
            val feedUrl = json.optString("feedUrl", null)
            return PodcastSearchResult(title, imageUrl, feedUrl)
        }

        /**
         * Constructs a Podcast instance from iTunes toplist entry
         *
         * @param json object holding the podcast information
         * @throws JSONException
         */
        @Throws(JSONException::class)
        fun fromItunesToplist(json: JSONObject): PodcastSearchResult {
            val title = json.getJSONObject("title").getString("label")
            var imageUrl: String? = null
            val images = json.getJSONArray("im:image")
            var i = 0
            while (imageUrl == null && i < images.length()) {
                val image = images.getJSONObject(i)
                val height = image.getJSONObject("attributes").getString("height")
                if (height.toInt() >= 100) {
                    imageUrl = image.getString("label")
                }
                i++
            }
            val feedUrl = "https://itunes.apple.com/lookup?id=" +
                    json.getJSONObject("id").getJSONObject("attributes").getString("im:id")
            return PodcastSearchResult(title, imageUrl, feedUrl)
        }
    }

}