package com.maruro.newspaper.utils

import android.content.SharedPreferences

class PreferencesWrapper(val preferences: SharedPreferences) {
    fun getValue(key: String, default: String = ""): String{
        return preferences.getString(key, default)!!
    }

    fun setValue(key: String, value: String){
        preferences.edit().putString(key, value).apply()
    }

    companion object{
        const val COUNTRY_SETTING = "country-setting"
    }
}