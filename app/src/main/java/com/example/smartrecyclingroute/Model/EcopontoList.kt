package com.example.smartrecyclingroute.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EcopontoList(
    @SerializedName("data")
    @Expose
    val data: List<Ecoponto>
)