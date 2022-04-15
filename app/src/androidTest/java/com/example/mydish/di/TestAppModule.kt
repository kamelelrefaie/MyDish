package com.example.mydish.di

import android.content.Context
import androidx.room.Room
import com.example.mydish.model.local.FavDishRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideFavDishDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, FavDishRoomDatabase::class.java)
            .allowMainThreadQueries().build()
}