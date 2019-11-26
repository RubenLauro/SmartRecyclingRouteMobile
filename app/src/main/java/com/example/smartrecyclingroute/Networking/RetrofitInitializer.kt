package com.example.smartrecyclingroute.Networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://68.183.44.22/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun services(): Services {
        return retrofit.create(Services::class.java)
    }
}