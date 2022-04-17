package com.example.mydish.view.viewmodel

import androidx.lifecycle.*
import com.example.mydish.repository.FavDishRepository
import com.example.mydish.model.remote.responses.FavDish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavDishViewModel @Inject constructor(private val repository: FavDishRepository) :
    ViewModel() {
    //livedata
    //val allDishesList: LiveData<List<FavDish>> = repository.allDishesList().asLiveData()
    //val favDishes: LiveData<List<FavDish>> = repository.getFavDishesList().asLiveData()

    val allDishesList = repository.allDishesList()
    val favDishes = repository.getFavDishesList()

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishDetails(dish)
    }

    fun getFilteredList(filterType: String): Flow<List<FavDish>> =
        repository.getFilterDishesList(filterType = filterType)

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishDetails(dish)
    }


}