package com.example.mydish.model.database

import androidx.annotation.WorkerThread
import com.example.mydish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishDetails(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun deleteFavDishDetails(favDish: FavDish) {
        favDishDao.deleteFavDishDetails(favDish)
    }

    fun allDishesList(): Flow<List<FavDish>> = favDishDao.getAllDishesList()

    fun getFavDishesList(): Flow<List<FavDish>> = favDishDao.getFavDishesList()

    fun getFilterDishesList(filterType: String) : Flow<List<FavDish>> = favDishDao.getFilterDishesList(filterType)

}