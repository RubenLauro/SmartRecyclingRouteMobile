package com.example.smartrecyclingroute.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ecoponto(
    @SerializedName("category")
    @Expose
    val category: String,
    @SerializedName("capacity")
    @Expose
    val capacity: Double
)