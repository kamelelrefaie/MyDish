package com.example.mydish.model.remote

import com.example.mydish.BuildConfig
import com.example.mydish.model.remote.responses.RandomDish
import com.example.mydish.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishApi{

    @GET(Constants.API_ENDPOINT)
    suspend fun getRandomDish(
        @Query(Constants.LIMIT_LICENSE) limitLicense: Boolean,
        @Query(Constants.TAGS) tags: String,
        @Query(Constants.NUMBER) number: Int,
        @Query(Constants.API_KEY) apiKey: String = BuildConfig.API_KEY
    ): RandomDish
}