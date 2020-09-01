package com.whywhom.soft.whyradiobox.model

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by wuhaoyong on 2020-01-15.
 */

data class PodcastSearchResult (
    val title: String? = null,
    val imageUrl: String? = null,
    val feedUrl: String? = null,
    val author: String? = null
){

    companion object {
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
            val author = json.optString("artistName", null)
            return PodcastSearchResult(title, imageUrl, feedUrl, author)
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
            var author: String? = null
            try {
                author = json.getJSONObject("im:artist").getString("label")
            } catch (e: Exception) {
                // Some feeds have empty artist
            }
            return PodcastSearchResult(title, imageUrl, feedUrl, author)
        }
        fun fromItunesEntry(item: Entry): PodcastSearchResult {
            val title: String? = item.title.label
            var imageUrl: String? = null
            val feedUrl: String? = "https://itunes.apple.com/lookup?id=" +
                    item.id.attributes.imId
            val author: String? = item.imArtist.label

            val images = item.imImage
            var i = 0
            while (imageUrl == null && i < images.size) {
                val image = images.get(i)
                val height = image.attributes.height
                if (height.toInt() >= 100) {
                    imageUrl = image.label
                }
                i++
            }
            return PodcastSearchResult(title, imageUrl, feedUrl, author)
        }
    }
}