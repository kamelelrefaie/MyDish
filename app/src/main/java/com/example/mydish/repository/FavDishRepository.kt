package com.example.mydish.repository

import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.model.remote.responses.RandomDish
import com.example.mydish.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavDishRepository {

    suspend fun insertFavDishDetails(favDish: FavDish)
    suspend fun deleteFavDishDetails(favDish: FavDish)
    fun allDishesList(): Flow<List<FavDish>>
    fun getFavDishesList(): Flow<List<FavDish>>
    fun getFilterDishesList(filterType: String): Flow<List<FavDish>>

    suspend fun getRandomDish(
        limitLicense: Boolean,
        tags: String,
        number: Int,
    ): Resource<RandomDish>

}