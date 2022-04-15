package com.example.mydish.repository

import androidx.annotation.WorkerThread
import com.example.mydish.model.local.FavDishDao
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.model.remote.responses.RandomDish
import com.example.mydish.model.remote.RandomDishApi
import com.example.mydish.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class DefaultFavDishRepository @Inject constructor(
    private val favDishDao: FavDishDao,
    private val randomDishApi: RandomDishApi
) : FavDishRepository {

    @WorkerThread
    override suspend fun insertFavDishDetails(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    override suspend fun deleteFavDishDetails(favDish: FavDish) {
        favDishDao.deleteFavDishDetails(favDish)
    }

    override fun allDishesList(): Flow<List<FavDish>> = favDishDao.getAllDishesList()

    override fun getFavDishesList(): Flow<List<FavDish>> = favDishDao.getFavDishesList()

    override fun getFilterDishesList(filterType: String): Flow<List<FavDish>> =
        favDishDao.getFilterDishesList(filterType)

    override suspend fun getRandomDish(
        limitLicense: Boolean,
        tags: String,
        number: Int
    ): Resource<RandomDish> {
        val response = try {
            randomDishApi.getRandomDish(limitLicense,tags,number,)
        } catch (e: Exception) {
            return Resource.Error(e.message!!)
        }

        return Resource.Success(response)
    }


}