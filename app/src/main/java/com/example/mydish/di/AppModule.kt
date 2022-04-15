package com.example.mydish.di

import android.content.Context
import androidx.room.Room
import com.example.mydish.model.local.FavDishDao
import com.example.mydish.model.local.FavDishRoomDatabase
import com.example.mydish.model.remote.RandomDishApi
import com.example.mydish.repository.DefaultFavDishRepository
import com.example.mydish.repository.FavDishRepository
import com.example.mydish.utils.Constants.BASE_URL
import com.example.mydish.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFavDishDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FavDishRoomDatabase::class.java, DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideFavDishDao(favDishRoomDatabase: FavDishRoomDatabase) =
        favDishRoomDatabase.favDishDao()


    @Singleton
    @Provides
    fun provideRandomDishApi(): RandomDishApi =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(RandomDishApi::class.java)

    @Singleton
    @Provides
    fun provideDefaultFavDishRepository(dao: FavDishDao, api: RandomDishApi) =
        DefaultFavDishRepository(dao, api) as FavDishRepository



}