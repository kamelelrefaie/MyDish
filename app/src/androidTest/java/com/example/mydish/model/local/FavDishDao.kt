package com.example.mydish.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mydish.model.remote.responses.FavDish
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class FavDishDaoTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var favDishRoomDatabase: FavDishRoomDatabase
    private lateinit var favDishDao: FavDishDao

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        favDishDao = favDishRoomDatabase.favDishDao()
    }

    @After
    fun tearDown() {
        favDishRoomDatabase.close()
    }

    @Test
    fun insertFavDish() = runBlockingTest {
        val favDish =
            FavDish(
                "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                false, 1
            )

        favDishDao.insertFavDishDetails(favDish)

        val getFavDish = favDishDao.getAllDishesList().first()
        assertThat(getFavDish).contains(favDish)

    }

    @Test
    fun deleteFavDishDetails() = runBlockingTest {
        val favDish =
            FavDish(
                "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                false, 1
            )

        favDishDao.insertFavDishDetails(favDish)
        favDishDao.deleteFavDishDetails(favDish)

        val getFavDish = favDishDao.getAllDishesList().first()
        assertThat(getFavDish).doesNotContain(favDish)

    }

    @Test
    fun getFavDishesList() = runBlockingTest {
        val favDish =
            FavDish(
                "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                true, 1
            )

        favDishDao.insertFavDishDetails(favDish)

        val getFavDish = favDishDao.getFavDishesList().first()
        assertThat(getFavDish).contains(favDish)

    }

    @Test
    fun getFilterDishesList() = runBlockingTest {
        val favDish =
            FavDish(
                "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                true, 1
            )

        favDishDao.insertFavDishDetails(favDish)

        val getFavDish = favDishDao.getFilterDishesList("kamel").first()
        assertThat(getFavDish).contains(favDish)

    }


}