package com.mussayev.sudoku.theme.provider

import android.content.Context
import android.view.View
import com.mussayev.sudoku.R

class DarkThemeProvider(private val context: Context) : ThemeProvider {
    override fun isDarkTheme(): Boolean = true
    override fun getTheme(): Int = R.style.Theme_Dark
    override fun getStatusBarColor(): Int = R.color.dark_grey_900
    override fun getBackgroundColor(): Int = R.drawable.background_app

    override fun getButtonStyle(): Int {
        TODO("Not yet implemented")
    }

    override fun updateResources(view: View) {
        TODO("Not yet implemented")
    }

    override fun updateResourcesHome(view: View) {
    }
}