package com.berkaykbl.movist.model

import com.google.gson.annotations.SerializedName

data class TVSerie(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("original_name")val original_name: String = "",
    @SerializedName("poster_path")val poster_path: String = "",
    @SerializedName("vote_average")val vote_average: Double = 0.0,
    @SerializedName("genre_ids")val genre_ids: List<Int> = ArrayList()
)
