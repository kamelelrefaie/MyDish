package com.example.mydish.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydish.model.remote.responses.RandomDish
import com.example.mydish.repository.FavDishRepository
import com.example.mydish.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RandomDishViewModel @Inject constructor(private val repository: FavDishRepository) :
    ViewModel() {


    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish>()
    val randomDishLoadingError = MutableLiveData<Boolean>()


    fun getRandomDishFromAPI() {
        loadRandomDish.value = true


        viewModelScope.launch {
            try {
                randomDishResponse.value = repository.getRandomDish(
                    Constants.LIMIT_LICENSE_VALUE,
                    Constants.TAGS_VALUE,
                    Constants.NUMBER_VALUE
                ).data!!
                loadRandomDish.value = false
            } catch (e: Exception) {
                randomDishLoadingError.value = true
                loadRandomDish.value = false
            }

        }


    }
}