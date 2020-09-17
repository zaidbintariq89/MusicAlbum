package com.bell.youtubeplayer.manager

import android.content.Context
import android.content.SharedPreferences
import com.bell.youtubeplayer.BellApplication
import com.bell.youtubeplayer.utils.Constants


class SharedPrefsManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: SharedPrefsManager? = null

        fun getInstance(): SharedPrefsManager {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = SharedPrefsManager()
                }
            }
            return INSTANCE!!
        }
    }

    private val sharedPreferences: SharedPreferences =
        BellApplication.getApplication().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )!!

    fun putData(key: String, data: Int) {
        sharedPreferences.edit().putInt(key, data).apply()
    }

    fun getData(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun putData(key: String, data: String) {
        sharedPreferences.edit().putString(key, data).apply()
    }

    fun getStringData(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    fun saveBoolean(key: String, flag: Boolean) {
        sharedPreferences.edit().putBoolean(key, flag).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putData(key: String, data: Long) {
        sharedPreferences.edit().putLong(key, data).apply()
    }

    fun getLongData(key: String): Long {
        return sharedPreferences.getLong(key, 0)
    }

    fun clearPrefData() {
        sharedPreferences.edit().clear().apply()
    }
}