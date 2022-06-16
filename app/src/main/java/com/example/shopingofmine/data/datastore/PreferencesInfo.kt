package com.example.shopingofmine.data.datastore

class PreferencesInfo(
    val theme: Theme = Theme.LIGHT,
    val customerId: Int? = null,
    val notificationInterval: Int = 3,
    val cartProductsCount: Int = 0
)