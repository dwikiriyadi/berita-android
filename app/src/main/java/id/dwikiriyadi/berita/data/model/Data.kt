package id.dwikiriyadi.berita.data.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id") val _id: String,
    @SerializedName("data") val data: Berita,
    @SerializedName("url") val url: String,
    @SerializedName("originalUrl") val originalUrl: String
)