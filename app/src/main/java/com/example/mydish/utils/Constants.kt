package com.example.mydish.utils


object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"
    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"

    const val EXTRA_DISH_DETAILS = "DishDetails"

    const val ALL_ITEMS = "All"
    const val FILTER_SELECTION = "Filter Selection"

    const val API_ENDPOINT = "recipes/random"

    const val API_KEY = "apiKey"
    const val TAGS = "tags"
    const val NUMBER = "number"
    const val LIMIT_LICENSE = "limitLicense"

    const val TAGS_VALUE = "vegetarian, dessert"
    const val NUMBER_VALUE = 1
    const val LIMIT_LICENSE_VALUE = true

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY_VALUE = "e5c13aea1eee4d0383914317042b0f86"

    const val DATABASE_NAME = "fav_dish_database"


    const val NOTIFICATION_ID = "FavDish_notification_id"
    const val NOTIFICATION_NAME = "FavDish"
    const val NOTIFICATION_CHANNEL_id = "FavDish_channel_01"


    fun dishTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")
        return list
    }

    fun dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Juices")
        list.add("Sandwich")
        list.add("Tea & Coffee")
        list.add("Wraps")
        list.add("Other")
        return list
    }

    fun dishCookTime(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }

}