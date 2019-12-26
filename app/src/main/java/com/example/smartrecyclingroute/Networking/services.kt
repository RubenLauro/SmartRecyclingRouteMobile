package com.example.smartrecyclingroute.Networking

import com.example.smartrecyclingroute.Model.EcopontoList
import com.example.smartrecyclingroute.Model.GroupList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Services {
    @GET("groups")
    fun listGroups(): Call<GroupList>

    @GET("ecopontos/{group}")
    fun listEcopontos(@Path("group")group_name : String): Call<EcopontoList>

    @Multipart
    @POST("reports")
    fun createReport(@Part file: MultipartBody.Part,
                                 @Part("group_name") groupName: RequestBody,
                                 @Part("lat") lat: RequestBody,
                                 @Part("lon") lon: RequestBody,
                                 @Part("description") description: RequestBody,
                                 @Part("type") type: RequestBody): Call<ResponseBody>
}