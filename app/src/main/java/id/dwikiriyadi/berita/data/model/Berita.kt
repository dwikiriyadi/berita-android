package id.dwikiriyadi.berita.data.model

import com.google.gson.annotations.SerializedName

data class Berita(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
    @SerializedName("image_cover") val image_cover: String,
    @SerializedName("image") val image: ArrayList<String>,
    @SerializedName("category") val category: String,
    @SerializedName("tag") val tag: ArrayList<String>,
    @SerializedName("source") val source: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("body") val body: String,
    @SerializedName("link") val link: String,
    @SerializedName("lastSlug") val lastSlug: String
)