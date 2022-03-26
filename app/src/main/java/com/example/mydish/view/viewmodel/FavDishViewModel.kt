package com.example.mydish.view.viewmodel

import androidx.lifecycle.*
import com.example.mydish.model.database.FavDishRepository
import com.example.mydish.model.entities.FavDish
import kotlinx.coroutines.launch


class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {

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

    class FavDishViewModelFactory(private val repository: FavDishRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
                return FavDishViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }

    }
}