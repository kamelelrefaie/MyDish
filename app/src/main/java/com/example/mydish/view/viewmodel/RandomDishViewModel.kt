package com.example.mydish.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydish.model.entities.RandomDish
import com.example.mydish.model.network.RandomDishApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.newThread


class RandomDishViewModel : ViewModel() {
    private val randomRecipeApiService = RandomDishApiService()

    private val compositeDisposable = CompositeDisposable()

    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishLoadingError = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish.Recipes>()

    fun getRandomRecipeFromAPI() {
        loadRandomDish.value = true

        compositeDisposable.add(
            randomRecipeApiService.getRandomDish().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RandomDish.Recipes>() {
                    override fun onSuccess(value: RandomDish.Recipes) {
                        loadRandomDish.value = false
                        randomDishLoadingError.value = false
                        randomDishResponse.value = value

                    }

                    override fun onError(e: Throwable) {
                        randomDishLoadingError.value = true
                        loadRandomDish.value = false

                    }

                })
        )
    }
}