package com.example.mydish.model.remote.responses

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)