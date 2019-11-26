package com.example.smartrecyclingroute.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("lat")
    @Expose
    val lat: Double,
    @SerializedName("lon")
    @Expose
    val lon: Double,
    @SerializedName("name")
    @Expose
    val name: String
)