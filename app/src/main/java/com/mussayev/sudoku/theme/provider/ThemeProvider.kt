package com.mussayev.sudoku.theme.provider

import android.view.View

interface ThemeProvider {
    fun isDarkTheme(): Boolean
    fun getTheme(): Int
    fun getStatusBarColor(): Int
    fun getBackgroundColor(): Int
    fun getButtonStyle(): Int
    fun updateResources(view: View)
    fun updateResourcesHome(view: View)
}