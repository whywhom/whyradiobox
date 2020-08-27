package com.whywhom.soft.whyradiobox.model

/**
 * Created by wuhaoyong on 25/08/20.
 */
data class ItunesSearchPodcast(
    val resultCount: Int,
    val results: List<SearchResult>
)

data class SearchResult(
    val artistId: Int,
    val artistName: String,
    val artistViewUrl: String,
    val artworkUrl100: String,
    val artworkUrl30: String,
    val artworkUrl60: String,
    val artworkUrl600: String,
    val collectionCensoredName: String,
    val collectionExplicitness: String,
    val collectionHdPrice: Int,
    val collectionId: Int,
    val collectionName: String,
    val collectionPrice: Double,
    val collectionViewUrl: String,
    val contentAdvisoryRating: String,
    val country: String,
    val currency: String,
    val feedUrl: String,
    val genreIds: List<String>,
    val genres: List<String>,
    val kind: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCensoredName: String,
    val trackCount: Int,
    val trackExplicitness: String,
    val trackHdPrice: Int,
    val trackHdRentalPrice: Int,
    val trackId: Int,
    val trackName: String,
    val trackPrice: Double,
    val trackRentalPrice: Int,
    val trackViewUrl: String,
    val wrapperType: String
)