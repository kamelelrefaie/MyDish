package com.example.mydish.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mydish.MainCoroutineRule
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.repository.FakeFavDishRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class FavDishViewModelTest {
    private lateinit var viewModel: FavDishViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule.MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = FavDishViewModel(FakeFavDishRepository())
    }


    @Test
    fun `insert item return true`() {
        runBlockingTest {
            val favDish =
                FavDish(
                    "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                    true, 1
                )

            viewModel.insert(favDish)
            assertThat(viewModel.allDishesList.first()).contains(favDish)
        }
    }

    @Test
    fun `delete item return true`() {
        runBlockingTest {
            val favDish =
                FavDish(
                    "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                    true, 1
                )

            viewModel.insert(favDish)
            viewModel.delete(favDish)
            assertThat(viewModel.allDishesList.first()).doesNotContain(favDish)
        }
    }    @Test
    fun `get filter items return true`() {
        runBlockingTest {
            val favDish =
                FavDish(
                    "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel", "kamel",
                    false, 1
                )

            viewModel.insert(favDish)

            assertThat(viewModel.favDishes.first()).contains(favDish)
        }
    }

}