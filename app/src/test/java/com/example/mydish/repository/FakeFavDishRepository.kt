package com.example.mydish.repository

import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.model.remote.responses.RandomDish
import com.example.mydish.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeFavDishRepository : FavDishRepository {
    private val favDishItems = mutableListOf<FavDish>()
    private val getAllDish = MutableStateFlow(favDishItems)
    private val getAllFavDish = MutableStateFlow(favDishItems)
    private val getAllFilterDishesList = MutableStateFlow(favDishItems)
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertFavDishDetails(favDish: FavDish) {
        favDishItems.add(favDish)
        getAllDish.value = favDishItems
    }

    override suspend fun deleteFavDishDetails(favDish: FavDish) {
        favDishItems.remove(favDish)
        getAllDish.value = favDishItems

    }

    override fun allDishesList(): Flow<List<FavDish>> {
        return getAllDish
    }

    override fun getFavDishesList(): Flow<List<FavDish>> {
        for (item in favDishItems) {
            if (item.favouriteDish) getAllFavDish.value.add(item)
        }

        return getFavDishesList()
    }

    override fun getFilterDishesList(filterType: String): Flow<List<FavDish>> {
        for (item in favDishItems) {
            if (item.type == filterType) getAllFilterDishesList.value.add(item)
        }

        return getAllFilterDishesList
    }

    override suspend fun getRandomDish(
        limitLicense: Boolean,
        tags: String,
        number: Int
    ): Resource<RandomDish> {
        return if (shouldReturnNetworkError) {
            Resource.Error("Error")
        } else {
            Resource.Success(RandomDish(listOf()))
        }
    }
}