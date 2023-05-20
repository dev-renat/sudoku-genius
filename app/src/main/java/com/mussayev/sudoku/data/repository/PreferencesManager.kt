package com.mussayev.sudoku.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.mussayev.sudoku.data.model.Theme
import com.mussayev.sudoku.data.model.Difficulty

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    //
    fun saveDifficulty(level: String) {
        sharedPreferences.edit().putString("level", level).apply()
    }

    fun getDifficulty(): String? {
        return sharedPreferences.getString("level", Difficulty.BEGINNER.name) // 1 - значение по умолчанию
    }

    //
    fun saveTheme(value: String) {
        sharedPreferences.edit().putString("theme", value).apply()
    }

    fun getTheme(): String? {
        return sharedPreferences.getString("theme", Theme.LIGHT.name)
    }

    //
    fun incrementLaunches() {
        sharedPreferences.edit().putLong("num_launches", getNumLaunches()).apply()
    }

    fun setNumLaunches(num: Long) {
        sharedPreferences.edit().putLong("num_launches", num).apply()
    }

    fun getNumLaunches(): Long {
        return sharedPreferences.getLong("num_launches", 0) + 1
    }

    //
//    fun saveUserScore(gameData: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("save_user_score", gameData)
//        editor.apply()
//    }
//
//    fun loadUserScore(): String? {
//        return sharedPreferences.getString("save_user_score", null)
//    }

    //
    fun saveGameData(level: String, gameData: String) {
        val editor = sharedPreferences.edit()
        editor.putString("game_data_level_$level", gameData)
        editor.apply()
    }

    fun loadGameData(level: String): String? {
        return sharedPreferences.getString("game_data_level_$level", null)
    }

    fun clearGameData(level: String) {
        with(sharedPreferences.edit()) {
            remove("game_data_level_$level")
            apply()
        }
    }

    // Statistics
    fun saveStatistics(level: String, gameData: String) {
        val editor = sharedPreferences.edit()
        editor.putString("game_statistics_level_$level", gameData)
        editor.apply()
    }

    fun loadStatistics(level: String): String? {
        return sharedPreferences.getString("game_statistics_level_$level", null)
    }

    //
    var isSoundEnabled: Boolean
        get() = sharedPreferences.getBoolean("sound_enabled", true) // Значение по умолчанию - true (включено)
        set(value) {
            sharedPreferences.edit().putBoolean("sound_enabled", value).apply()
        }

    var isMusicEnabled: Boolean
        get() = sharedPreferences.getBoolean("music_enabled", true) // Значение по умолчанию - true (включено)
        set(value) {
            sharedPreferences.edit().putBoolean("music_enabled", value).apply()
        }

    var isVibrationEnabled: Boolean
        get() = sharedPreferences.getBoolean("vibration_enabled", true) // Значение по умолчанию - true (включено)
        set(value) {
            sharedPreferences.edit().putBoolean("vibration_enabled", value).apply()
        }

    var isErrorLimitEnabled: Boolean
        get() = sharedPreferences.getBoolean("error_limit_enabled", true) // Значение по умолчанию - true (включено)
        set(value) {
            sharedPreferences.edit().putBoolean("error_limit_enabled", value).apply()
        }
}