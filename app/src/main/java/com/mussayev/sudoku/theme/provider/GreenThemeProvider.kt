package com.mussayev.sudoku.theme.provider

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.mussayev.sudoku.R

class GreenThemeProvider(private val context: Context) : ThemeProvider {
    override fun isDarkTheme(): Boolean = false
    override fun getTheme(): Int = R.style.Theme_Green
    override fun getStatusBarColor(): Int = R.color.md_light_green_500
    override fun getBackgroundColor(): Int = R.drawable.background_app

    override fun getButtonStyle(): Int {
        TODO("Not yet implemented")
    }

    override fun updateResources(view: View) {
        TODO("Not yet implemented")
    }

    override fun updateResourcesHome(view: View) {
//        val decrementLevelButton = view.findViewById<ImageView>(R.id.decrement_level_button)
//        decrementLevelButton.setColorFilter(ContextCompat.getColor(context, R.color.green_400), android.graphics.PorterDuff.Mode.MULTIPLY)
//
//        val incrementLevelButton = view.findViewById<ImageView>(R.id.increment_level_button)
//        incrementLevelButton.setColorFilter(ContextCompat.getColor(context, R.color.green_400), android.graphics.PorterDuff.Mode.MULTIPLY)
    }
}