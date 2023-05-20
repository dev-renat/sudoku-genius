package com.mussayev.sudoku.ui.custom

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import androidx.annotation.AttrRes
import com.mussayev.sudoku.R

class SudokuBoardStyle(private val context: Context) {
    val cellMarginPixels = 3
    val squareMarginPixels = 10
    val cornerRadius = 12f

    // Кисть для рисования жирных линий
    val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = getColorFromAttr(R.attr.thickLinePaintColor)
        strokeWidth = 4F
    }

    // Кисть для рисования тонких линий
    val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = getColorFromAttr(R.attr.thinLinePaintColor)
        strokeWidth = 2F
    }

    // Кисть для закрашивания начальных ячеек
    val cellPaintStarting = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorStarting)
    }

    // Кисть для рисования текста начальных ячеек
    val cellPaintTextStarting = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorStarting)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontStarting)
    }

    // Кисть для закрашивания выбранной ячейки
    val cellPaintSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorSelected)
    }

    // Кисть для рисования текста выбранной ячейки
    val cellPaintTextSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorSelected)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontSelected)
    }

    // Кисть для закрашивания конфликтующих ячеек
    val cellPaintConflicting = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorConflicting)
        //alpha = 70
    }

    // Кисть для рисования текста конфликтующих ячеек
    val cellPaintTextConflicting = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorConflicting)
    }

    // CELL SUCCESS
    val cellPaintSuccess = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorSuccess)
        //alpha = 0
    }

    val cellPaintTextSuccess = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorSuccess)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontSuccess)
    }

    // CELL SUCCESS SELECTED
    val cellPaintSuccessSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorSuccessSelected)
    }
    val cellPaintTextSuccessSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorSuccessSelected)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontSuccessSelected)
    }

    // CELL HINT
    val cellPaintHint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorHint)
        //alpha = 0
    }

    val cellPaintTextHint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorHint)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontHint)
    }

    // CELL HINT SELECTED
    val cellPaintHintSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorHintSelected)
        //alpha = 0
    }
    val cellPaintTextHintSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorHintSelected)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontHintSelected)
    }

    // CELL ERROR
    val cellPaintError = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorError)
        //alpha = 0
    }

    val cellPaintTextError = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorError)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontError)
    }

    // CELL ERROR SELECTED
    val cellPaintErrorSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorErrorSelected)
    }

    val cellPaintTextErrorSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorErrorSelected)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontErrorSelected)
    }

    // CELL EMPTY
    val cellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColor)
    }

    val cellPaintText = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColor)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFont)
    }

    // CELL NOTE
    val cellPaintNote = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorNote)
    }

    val cellPaintTextNote = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorNote)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontNote)
    }

    // CELL NOTE SELECTED
    val cellPaintNoteSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintBGColorNoteSelected)
    }

    val cellPaintTextNoteSelected = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = getColorFromAttr(R.attr.cellPaintTextColorNoteSelected)
        typeface = getTypefaceFromAttr(R.attr.cellPaintTextFontNoteSelected)
    }

    // BOARD BACKGROUND
    val boardBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = getColorFromAttr(R.attr.boardBackgroundColor) // Получите цвет фона из атрибутов или определите его самостоятельно
    }


    // Метод для получения атрибутов из темы приложения
    private fun getColorFromAttr(attr: Int): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle() // Закрытие TypedArray
        return color
    }

    // Метод для получения шрифта из атрибутов
    private fun getTypefaceFromAttr(@AttrRes attr: Int): Typeface? {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attr))
        val typeface = try {
            typedArray.getFont(0)
        } finally {
            typedArray.recycle() // Закрытие TypedArray
        }
        return typeface
    }

}