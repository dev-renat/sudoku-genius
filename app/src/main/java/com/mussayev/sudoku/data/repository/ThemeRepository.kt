package com.mussayev.sudoku.data.repository

import android.content.Context
import com.mussayev.sudoku.data.model.Theme
import com.mussayev.sudoku.theme.provider.*

class ThemeRepository(private val settingsRepository: SettingsRepository) {

    fun createThemeProvider(context: Context): ThemeProvider {
        return when (settingsRepository.currentThemeName()) {
            Theme.LIGHT.name -> LightThemeProvider(context)
            Theme.DARK.name -> DarkThemeProvider(context)
            Theme.DARK_GREY.name -> DarkGreyThemeProvider(context)
            Theme.GREEN.name -> GreenThemeProvider(context)
            Theme.LEMONADE.name -> LemonadeThemeProvider(context)
            else -> LightThemeProvider(context)
        }
    }

    fun saveTheme(theme: Theme) {
        settingsRepository.saveTheme(theme.name)
    }

    fun currentThemeName(): String? {
        return settingsRepository.currentThemeName()
    }
}