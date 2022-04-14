package com.example.mydish.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mydish.model.remote.responses.FavDish

@Database(entities = [FavDish::class], version = 1, exportSchema = false)
abstract class FavDishRoomDatabase :RoomDatabase() {
    abstract fun favDishDao(): FavDishDao

}