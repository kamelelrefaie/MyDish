package com.example.mydish.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mydish.MainCoroutineRule
import com.example.mydish.repository.FakeFavDishRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RandomDishViewModelTest {
    private lateinit var viewModel: RandomDishViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule.MainCoroutineRule()
    val fake = FakeFavDishRepository()

    @Before
    fun setup() {

        viewModel = RandomDishViewModel(fake)
    }

    @Test
    fun `api dosen't work fine return true`() {
        fake.setShouldReturnNetworkError(true)
        viewModel.getRandomDishFromAPI()
        assertThat(viewModel.randomDishResponse.value).isNull()
    }

    @Test
    fun `api  work fine return true`() {
        viewModel.getRandomDishFromAPI()
        assertThat(viewModel.randomDishResponse.value).isNotNull()
    }

}