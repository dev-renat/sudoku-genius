package com.mussayev.sudoku.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.mussayev.sudoku.R

class DifficultySelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val difficultyLiveData = MutableLiveData<Int>()

    init {
        LayoutInflater.from(context).inflate(R.layout.difficulty_selector, this, true)
        orientation = HORIZONTAL

        val decrementDifficultyButton: ImageButton = findViewById(R.id.decrementDifficultyButton)
        val incrementDifficultyButton: ImageButton = findViewById(R.id.incrementDifficultyButton)

        decrementDifficultyButton.setOnClickListener {
            decrementDifficulty()
        }

        incrementDifficultyButton.setOnClickListener {
            incrementDifficulty()
        }
    }

    fun setDifficulty(difficulty: Int) {
        difficultyLiveData.value = difficulty
    }

    private fun decrementDifficulty() {
        difficultyLiveData.value?.let { currentDifficulty ->
            difficultyLiveData.value = currentDifficulty - 1
        }
    }

    private fun incrementDifficulty() {
        difficultyLiveData.value?.let { currentDifficulty ->
            difficultyLiveData.value = currentDifficulty + 1
        }
    }

    fun observe(listener: (Int) -> Unit) {
        difficultyLiveData.observeForever(listener)
    }
}
