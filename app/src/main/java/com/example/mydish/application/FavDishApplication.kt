package com.example.mydish.application

import android.app.Application
import com.example.mydish.model.database.FavDishRepository
import com.example.mydish.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {
    private val database by lazy { FavDishRoomDatabase.getDatabase(this) }
    val repository by lazy { FavDishRepository(database.favDishDao()) }
}