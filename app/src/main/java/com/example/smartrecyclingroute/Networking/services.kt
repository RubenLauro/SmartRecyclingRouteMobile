package com.example.smartrecyclingroute.Networking

import com.example.smartrecyclingroute.Model.EcopontoList
import com.example.smartrecyclingroute.Model.GroupList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {
    @GET("groups")
    fun listGroups(): Call<GroupList>

    @GET("ecopontos/{group}")
    fun listEcopontos(@Path("group")group_name : String): Call<EcopontoList>
}