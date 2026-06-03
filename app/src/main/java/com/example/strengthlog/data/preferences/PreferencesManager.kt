package com.example.strengthlog.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "strengthlog_prefs"
        private const val KEY_WEIGHT_UNIT = "weight_unit"
        const val UNIT_KG = "kg"
        const val UNIT_LBS = "lbs"
    }

    var weightUnit: String
        get() = sharedPreferences.getString(KEY_WEIGHT_UNIT, UNIT_KG) ?: UNIT_KG
        set(value) {
            sharedPreferences.edit().putString(KEY_WEIGHT_UNIT, value).apply()
        }
}