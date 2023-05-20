package com.mussayev.sudoku.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.Cell
import kotlin.math.min

class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var useImages: Boolean = false
    private var cellImages: Array<Bitmap?> = arrayOfNulls(9)
    private var imagesResId: Int = 0

    init {
        val attrsArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.SudokuBoardView, 0, 0)
        imagesResId = attrsArray.getResourceId(R.styleable.SudokuBoardView_cellImages, 0)
        attrsArray.recycle()

        val typedArray = context.resources.obtainTypedArray(imagesResId)
        for (i in 0 until typedArray.length()) {
            cellImages[i] = BitmapFactory.decodeResource(context.resources, typedArray.getResourceId(i, 0))
        }
        typedArray.recycle()
    }

    // Количество блоков (квадратных областей) на доске
    private var sqrtSize = 3
    // Количество ячеек на доске
    private var size = 9

    // Размер ячейки в пикселях
    private var cellSizePixels = 0F
    // Размер заметки в пикселях
    private var noteSizePixels = 0F

    // Выбранная строка
    private var selectedRow = 0
    // Выбранная колонка
    private var selectedCol = 0
    // Слушатель касания экрана
    private var listener: OnTouchListener? = null

    // Объект для отсечения углов
    private val clipPath = Path()
    // Список ячеек
    private var cells: List<Cell>? = null

    //
    private var isNoteMode = false

    private val boardStyle = SudokuBoardStyle(context)

    // Метод для определения размера View
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    // Метод для отрисовки ячеек
    override fun onDraw(canvas: Canvas) {
        //drawBoardBackground(canvas)
        updateMeasurements(width)
        //clipRoundRect(canvas)
        fillCells(canvas)
        //drawLines(canvas)

        if (useImages) {
            drawImages(canvas)
        } else {
            drawText(canvas)
        }
    }

    private fun clipRoundRect(canvas: Canvas) {
        val cornerRadius = 20f
        val rect = RectF(0F, 0F, width.toFloat(), height.toFloat())
        clipPath.reset()
        clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = (width - 2 * boardStyle.squareMarginPixels) / size.toFloat()
        noteSizePixels = cellSizePixels / sqrtSize.toFloat()

        boardStyle.cellPaintText.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextSelected.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextStarting.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextConflicting.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextSuccess.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextSuccessSelected.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextHint.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextError.textSize = cellSizePixels / 1.8F
        boardStyle.cellPaintTextErrorSelected.textSize = cellSizePixels / 1.8F

        boardStyle.cellPaintTextNote.textSize = cellSizePixels / sqrtSize.toFloat()
        boardStyle.cellPaintTextNoteSelected.textSize = cellSizePixels / sqrtSize.toFloat()
    }

    private fun fillCells(canvas: Canvas) {

        cells?.forEach {
            val r = it.row
            val c = it.col

//            if (selectedRow > -1 && selectedCol > -1 && (r == selectedRow || c == selectedCol)) {
//                fillCell(canvas, r, c, cellPaintConflicting)
//            }
//            else
            if (r == selectedRow && c == selectedCol) {
                if (it.isHint && it.isSuccess == true) {
                    fillCell(canvas, r, c, boardStyle.cellPaintHintSelected)
                }
                else if (it.isSuccess == false) {
                    fillCell(canvas, r, c, boardStyle.cellPaintErrorSelected)
                }
                else if (it.isSuccess == true) {
                    fillCell(canvas, r, c, boardStyle.cellPaintSuccessSelected)
                }
                else if (it.notes.isNotEmpty() || isNoteMode) {
                    fillCell(canvas, r, c, boardStyle.cellPaintNoteSelected)
                }
                else {
                    fillCell(canvas, r, c, boardStyle.cellPaintSelected)
                }
            }
            else if (it.isStarting) {
                fillCell(canvas, r, c, boardStyle.cellPaintStarting)
            }
            else if (it.isSuccess == false) {
                fillCell(canvas, r, c, boardStyle.cellPaintError)
            }
            else if (it.isSuccess == true && it.isHint) {
                fillCell(canvas, r, c, boardStyle.cellPaintHint)
            }
            else if (it.isSuccess == true) {
                fillCell(canvas, r, c, boardStyle.cellPaintSuccess)
            }
            else if (it.notes.isNotEmpty() ?: false) {
                fillCell(canvas, r, c, boardStyle.cellPaintNote)
            }
            else if ((r == selectedRow || c == selectedCol) && !isNoteMode) {
                fillCell(canvas, r, c, boardStyle.cellPaintConflicting)
            }
            else if (
                r / sqrtSize == selectedRow / sqrtSize &&
                c / sqrtSize == selectedCol / sqrtSize &&
                selectedRow > 0 && selectedCol > 0 && !isNoteMode
            ) {
                fillCell(canvas, r, c, boardStyle.cellPaintConflicting)
            }
            else {
                fillCell(canvas, r, c, boardStyle.cellPaint)
            }

//            if (r == selectedRow && c == selectedCol && it.isSuccess == true) {
//                fillCell(canvas, r, c, selectedSuccessCellPaint)
//            }
//            else if (r == selectedRow && c == selectedCol) {
//                fillCell(canvas, r, c, selectedCellPaint)
//            }
//            else if (r == selectedRow || c == selectedCol) {
//                fillCell(canvas, r, c, conflictingCellPaint)
//            }
//            else if (
//                selectedRow > 0 && selectedCol > 0 &&
//                r / sqrtSize == selectedRow / sqrtSize &&
//                c / sqrtSize == selectedCol / sqrtSize
//            ) {
//                fillCell(canvas, r, c, conflictingCellPaint)
//            }

//            if (it.isStarting) {
//                fillCell(canvas, r, c, startingCellPaint)
//            }
//            else if (it.isSuccess == false) {
//                fillCell(canvas, r, c, errorCellPaint)
//            }
//            else if (r == selectedRow && c == selectedCol) {
//                fillCell(canvas, r, c, selectedCellPaint)
//            }
//            else if (r == selectedRow || c == selectedCol) {
//                fillCell(canvas, r, c, conflictingCellPaint)
//            }
//            else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize) {
//                fillCell(canvas, r, c, conflictingCellPaint)
//            }
//            else {
//                fillCell(canvas, r, c, startingCellPaint)
//            }
        }
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach { cell ->
            val value = cell.value
            val textBounds = Rect()
            val row = cell.row
            val col = cell.col

            val squareOffsetX = col / sqrtSize * boardStyle.squareMarginPixels
            val squareOffsetY = row / sqrtSize * boardStyle.squareMarginPixels

            if (value == 0) {
                // draw notes
                cell.notes.forEach { note ->
                    val rowInCell = (note - 1) / sqrtSize
                    val colInCell = (note - 1) % sqrtSize
                    val valueString = note.toString()

                    var paintToUse = boardStyle.cellPaintTextNote
                    if (row == selectedRow && col == selectedCol) {
                        paintToUse = boardStyle.cellPaintTextNoteSelected
                    }

                    paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                    val textWidth = paintToUse.measureText(valueString)
                    val textHeight = textBounds.height()

                    canvas.drawText(
                        valueString,
                        (col * cellSizePixels + squareOffsetX) + (colInCell * noteSizePixels) + noteSizePixels / 2 - textWidth / 2f,
                        (row * cellSizePixels + squareOffsetY) + (rowInCell * noteSizePixels) + noteSizePixels / 2 + textHeight / 2f,
                        paintToUse
                    )
                }

            } else {
                val valueString = cell.value.toString()

                val paintToUse = when {
                    cell.isStarting -> boardStyle.cellPaintTextStarting
                    //(cell.isSuccess == true && row == selectedRow && col == selectedCol) -> selectedSuccessCellTextPaint
                    cell.isSuccess == true && cell.isHint -> boardStyle.cellPaintTextHint
                    cell.isSuccess == true -> boardStyle.cellPaintTextSuccess
                    cell.isSuccess == false && row == selectedRow && col == selectedCol -> boardStyle.cellPaintTextErrorSelected
                    cell.isSuccess == false -> boardStyle.cellPaintTextError
                    else -> boardStyle.cellPaintText
                }

                paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = paintToUse.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(valueString,
                    (col * cellSizePixels + squareOffsetX) + cellSizePixels / 2 - textWidth / 2,
                    (row * cellSizePixels + squareOffsetY) + cellSizePixels / 2 + textHeight / 2,
                    paintToUse
                )
            }
        }
    }

    private fun drawImages(canvas: Canvas) {
        cells?.forEach { cell ->
            val value = cell.value
            val textBounds = Rect()
            val row = cell.row
            val col = cell.col

            val squareOffsetX = col / sqrtSize * boardStyle.squareMarginPixels
            val squareOffsetY = row / sqrtSize * boardStyle.squareMarginPixels


            if (value != 0) {
                // Отрисовка изображений
                val bitmap = cellImages[value - 1]
                bitmap?.let {
                    val imageMarginPixels = 10 // Задайте значение отступа, которое вам подходит

                    val cellCenterX = (col * cellSizePixels + squareOffsetX) + cellSizePixels / 2f
                    val cellCenterY = (row * cellSizePixels + squareOffsetY) + cellSizePixels / 2f

                    val halfImageWidth = (cellSizePixels - 2 * boardStyle.cellMarginPixels - 2 * imageMarginPixels) / 2f
                    val halfImageHeight = (cellSizePixels - 2 * boardStyle.cellMarginPixels - 2 * imageMarginPixels) / 2f

                    val left = cellCenterX - halfImageWidth
                    val top = cellCenterY - halfImageHeight
                    val right = cellCenterX + halfImageWidth
                    val bottom = cellCenterY + halfImageHeight

                    canvas.drawBitmap(it, null, RectF(left, top, right, bottom), null)
                }
            } else {
                cell.notes.forEach { note ->
                    val rowInCell = (note - 1) / sqrtSize
                    val colInCell = (note - 1) % sqrtSize

                    val bitmap = cellImages[note - 1]
                    bitmap?.let {
                        val left = (col * cellSizePixels + squareOffsetX) + (colInCell * noteSizePixels)
                        val top = (row * cellSizePixels + squareOffsetY) + (rowInCell * noteSizePixels)
                        val right = left + noteSizePixels
                        val bottom = top + noteSizePixels

                        canvas.drawBitmap(it, null, RectF(left, top, right, bottom), null)
                    }
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        val squareOffsetX = c / sqrtSize * boardStyle.squareMarginPixels
        val squareOffsetY = r / sqrtSize * boardStyle.squareMarginPixels

        val left = c * cellSizePixels + boardStyle.cellMarginPixels + squareOffsetX
        val top = r * cellSizePixels + boardStyle.cellMarginPixels + squareOffsetY
        val right = left + cellSizePixels - 2 * boardStyle.cellMarginPixels
        val bottom = top + cellSizePixels - 2 * boardStyle.cellMarginPixels

        canvas.drawRoundRect(left, top, right, bottom, boardStyle.cornerRadius, boardStyle.cornerRadius, paint)
    }

    private fun drawLines(canvas: Canvas) {
        for (i in 1 until size) {
            val paintToUse = when (i % sqrtSize) {
                0 -> boardStyle.thickLinePaint
                else -> boardStyle.thinLinePaint
            }
            for (j in 0 until size) {
                // Горизонтальные линии
                if (j != 0) {
                    val startX = j * cellSizePixels
                    val startY = i * cellSizePixels
                    val stopX = startX + cellSizePixels - (cellSizePixels / sqrtSize)
                    val stopY = startY
                    canvas.drawLine(startX, startY, stopX, stopY, paintToUse)
                }

                // Вертикальные линии
                if (j != size - 1) {
                    val startX = i * cellSizePixels
                    val startY = j * cellSizePixels
                    val stopX = startX
                    val stopY = startY + cellSizePixels - (cellSizePixels / sqrtSize)
                    canvas.drawLine(startX, startY, stopX, stopY, paintToUse)
                }
            }
        }
    }

    // Метод для обработки касаний экрана
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                performClick()
                true
            }
            else -> false
        }
    }

    fun changeNoteTakingState() {
        isNoteMode = !isNoteMode
        toggleNoteMode(isNoteMode)
    }

    override fun performClick(): Boolean {
        println("performClick")
        return super.performClick()
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        if (possibleSelectedRow in 0 until size && possibleSelectedCol in 0 until size) {
            listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
        }
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }

    private fun drawBoardBackground(canvas: Canvas) {
        val cornerRadius = 20f
        val rect = RectF(0F, 0F, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, boardStyle.boardBackgroundPaint)
    }

    fun toggleNoteMode(bool: Boolean) {
        isNoteMode = bool
        invalidate()
    }
}