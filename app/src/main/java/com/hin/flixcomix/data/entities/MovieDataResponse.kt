package com.hin.flixcomix.data.entities

import com.google.gson.annotations.SerializedName

data class MovieDataResponse(
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("data") val data: DataResponse? = null,
    @SerializedName("type_list") val typeList: String? = null,
)
