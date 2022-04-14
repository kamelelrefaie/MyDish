package com.example.mydish.view.viewmodel

import androidx.lifecycle.*
import com.example.mydish.repository.FavDishRepository
import com.example.mydish.model.remote.responses.FavDish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavDishViewModel @Inject constructor(private val repository: FavDishRepository) : ViewModel() {

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishDetails(dish)
    }

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList().asLiveData()
    val favDishes: LiveData<List<FavDish>> = repository.getFavDishesList().asLiveData()

    fun getFilteredList(filterType: String): LiveData<List<FavDish>> =
        repository.getFilterDishesList(filterType = filterType).asLiveData()


    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishDetails(dish)
    }


}