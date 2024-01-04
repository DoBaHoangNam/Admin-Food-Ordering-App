package com.example.adminfoodorderingapp.model

data class Order (
    var customerName: String? = null,
    var foodName: String? = null,
    var quantity: Int = 1,
    var foodImage: String? = null,
)