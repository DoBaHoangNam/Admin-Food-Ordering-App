package com.example.adminfoodorderingapp.model

data class Item (
    val itemKey: String? = null,
    val foodName: String? = null,
    val price: String? = null,
    val description: String? = null,
    val image: String? = null,
    val ingredient: String? = null,
    var quantity: Int = 1,
)