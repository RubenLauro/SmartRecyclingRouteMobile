package com.example.smartrecyclingroute.Networking

import com.example.smartrecyclingroute.Model.GroupList
import retrofit2.Call
import retrofit2.http.GET

interface Services {
    @GET("groups")
    fun listEcopontos(): Call<GroupList>
}