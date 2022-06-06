package com.example.shopingofmine.data.datastore

import androidx.appcompat.app.AppCompatDelegate

enum class Theme(val value: Int){
    NIGHT(AppCompatDelegate.MODE_NIGHT_YES),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
}