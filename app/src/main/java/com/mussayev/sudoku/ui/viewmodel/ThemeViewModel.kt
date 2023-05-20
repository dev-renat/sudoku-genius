package com.mussayev.sudoku.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mussayev.sudoku.data.model.Theme
import com.mussayev.sudoku.data.repository.ThemeRepository
import com.mussayev.sudoku.theme.provider.ThemeProvider

class ThemeViewModel(private val themeRepository: ThemeRepository) : ViewModel() {
    private lateinit var themeProvider: ThemeProvider

    fun createThemeProvider(context: Context) {
        themeProvider = themeRepository.createThemeProvider(context)
    }

    fun saveTheme(theme: Theme) {
        themeRepository.saveTheme(theme)
    }

    fun getThemeProvider(): ThemeProvider {
        return themeProvider
    }

    fun currentThemeName(): String? {
        return themeRepository.currentThemeName()
    }
}