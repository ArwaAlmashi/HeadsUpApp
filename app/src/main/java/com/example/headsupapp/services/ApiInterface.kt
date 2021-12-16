package com.example.headsupapp.services

import com.example.headsupapp.model.Celebrity
import com.example.headsupapp.model.CelebrityModel
import com.example.headsupapp.model.CelebrityModelItem
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    @GET("celebrities/")
//    fun getApiData(): Call<CelebrityModel>

    @GET("celebrities/")
        fun getApiData(): Call<CelebrityModel>

    @POST("celebrities/")
    fun postApiData(@Body celebrity: Celebrity): Call<Celebrity>

    @PUT("celebrities/{id}")
    fun updateApiData(@Path("id") id: Int, @Body person: CelebrityModelItem): Call<CelebrityModelItem>

    @DELETE("celebrities/{id}")
    fun deleteApiData(@Path("id") id: Int): Call<Void>
}