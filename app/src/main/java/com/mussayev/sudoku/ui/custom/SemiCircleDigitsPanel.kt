package com.mussayev.sudoku.ui.custom

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SemiCircleDigitsPanel(context: Context) : ViewGroup(context) {
    var onDigitClickListener: ((Int) -> Unit)? = null

    init {
        for (i in 1..9) {
            val button = Button(context).apply {
                text = i.toString()
                setOnClickListener { onDigitClickListener?.invoke(i) }
            }
            addView(button)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)

        val childMeasureSpec = View.MeasureSpec.makeMeasureSpec(width / 5, View.MeasureSpec.EXACTLY)
        for (i in 0 until childCount) {
            getChildAt(i).measure(childMeasureSpec, childMeasureSpec)
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val centerX = width / 2
        val centerY = height / 2
        val innerRadius = width / 3
        val outerRadius = width * 2 / 5

        for (i in 0 until childCount) {
            val button = getChildAt(i) as Button
            val angle = (2 * PI / 9) * i
            val radius = if (i < 5) innerRadius else outerRadius
            val x = centerX + (radius * cos(angle)).toInt() - button.measuredWidth / 2
            val y = centerY - (radius * sin(angle)).toInt() - button.measuredHeight / 2

            button.layout(x, y, x + button.measuredWidth, y + button.measuredHeight)
        }
    }

    fun showAt(point: Point) {
        x = point.x.toFloat() - width / 2
        y = point.y.toFloat() - height / 2
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }
}
