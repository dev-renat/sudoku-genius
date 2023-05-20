package com.mussayev.sudoku.utils

import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import kotlin.math.pow
import kotlin.math.sqrt

object ScreenSizeUtils {
    fun isScreenSizeLessThanInches(windowManager: WindowManager, inches: Int): Boolean {
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.displayCutout())
            val bounds = windowMetrics.bounds
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom
            val width = bounds.width() - insetsWidth
            val height = bounds.height() - insetsHeight

            displayMetrics.widthPixels = width
            displayMetrics.heightPixels = height
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        val x = (displayMetrics.widthPixels / displayMetrics.xdpi).toDouble().pow(2.0)
        val y = (displayMetrics.heightPixels / displayMetrics.ydpi).toDouble().pow(2.0)
        val screenSizeInches = sqrt(x + y)

        return screenSizeInches < inches
    }
}