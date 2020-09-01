package com.whywhom.soft.whyradiobox.model

import com.google.gson.annotations.SerializedName

/**
 * Created by wuhaoyong on 2020-01-14.
 */
data class ItunesTopPodcast(
    val feed: Feed
)

data class Feed(
    val author: Author,
    val entry: List<Entry>,
    val icon: Icon,
    val id: IdX,
    val link: List<LinkX>,
    val rights: RightsX,
    val title: TitleX,
    val updated: Updated
)

data class Author(
    val name: Name,
    val uri: Uri
)

data class Name(
    val label: String
)

data class Uri(
    val label: String
)

data class Entry(
    val category: Category,
    val id: Id,
    @SerializedName("im:artist")
    val imArtist: ImArtist,
    @SerializedName("im:contentType")
    val imContentType: ImContentType,
    @SerializedName("im:image")
    val imImage: List<ImImage>,
    @SerializedName("im:name")
    val imName: ImName,
    @SerializedName("im:price")
    val imPrice: ImPrice,
    @SerializedName("im:releaseDate")
    val imReleaseDate: ImReleaseDate,
    val link: Link,
    val rights: Rights,
    val summary: Summary,
    val title: Title
)

data class Category(
    val attributes: Attributes
)

data class Attributes(
    @SerializedName("im:id")
    val imId: String,
    val label: String,
    val scheme: String,
    val term: String
)

data class Id(
    val attributes: AttributesId,
    val label: String
)

data class AttributesId(
    @SerializedName("im:id")
    val imId: String
)

data class ImArtist(
    val attributes: AttributesImArtist,
    val label: String
)

data class AttributesImArtist(
    val href: String
)

data class ImContentType(
    val attributes: AttributesImContentType
)

data class AttributesImContentType(
    val label: String,
    val term: String
)

data class ImImage(
    val attributes: AttributesXXXX,
    val label: String
)

data class AttributesXXXX(
    val height: String
)

data class ImName(
    val label: String
)

data class ImPrice(
    val attributes: AttributesXXXXX,
    val label: String
)

data class AttributesXXXXX(
    val amount: String,
    val currency: String
)

data class ImReleaseDate(
    val attributes: AttributesXXXXXX,
    val label: String
)

data class AttributesXXXXXX(
    val label: String
)

data class Link(
    val attributes: AttributesXXXXXXX
)

data class AttributesXXXXXXX(
    val href: String,
    val rel: String,
    val type: String
)

data class Rights(
    val label: String
)

data class Summary(
    val label: String
)

data class Title(
    val label: String
)

data class Icon(
    val label: String
)

data class IdX(
    val label: String
)

data class LinkX(
    val attributes: AttributesXXXXXXXX
)

data class AttributesXXXXXXXX(
    val href: String,
    val rel: String
)

data class RightsX(
    val label: String
)

data class TitleX(
    val label: String
)

data class Updated(
    val label: String
)