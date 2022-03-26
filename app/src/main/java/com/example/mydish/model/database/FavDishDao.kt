package com.example.mydish.model.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.mydish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Delete
    suspend fun deleteFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM fav_dishes_table ORDER BY id")
     fun getAllDishesList() : Flow<List<FavDish>>

    @Query("SELECT * FROM fav_dishes_table WHERE favourite_dish = 1 ORDER BY id")
    fun getFavDishesList() : Flow<List<FavDish>>

    @Query("SELECT * FROM fav_dishes_table WHERE type = :filterType ORDER BY id")
    fun getFilterDishesList(filterType: String) : Flow<List<FavDish>>
}