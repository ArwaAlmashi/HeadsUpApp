package com.example.headsupapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dojo-recipes.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}