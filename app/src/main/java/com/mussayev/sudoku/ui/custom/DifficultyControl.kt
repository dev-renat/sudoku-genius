package com.mussayev.sudoku.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mussayev.sudoku.R
import com.mussayev.sudoku.ui.viewmodel.DifficultyViewModel
import com.mussayev.sudoku.utils.DifficultyUtils

//class DifficultyControl : LinearLayout {
//    private lateinit var decrementButton: ImageButton
//    private lateinit var incrementButton: ImageButton
//    private lateinit var difficultyTextView: TextView
//    private val difficultyLiveData = MutableLiveData<String>()
//
//    private lateinit var viewModel: DifficultyViewModel
//
//    constructor(context: Context) : super(context) {
//        init(context)
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        init(context)
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        init(context)
//    }
//
//    private fun init(context: Context) {
//        LayoutInflater.from(context).inflate(R.layout.custom_difficulty_control, this, true)
//
//        decrementButton = findViewById(R.id.decrement_difficulty_button)
//        incrementButton = findViewById(R.id.increment_difficulty_button)
//        difficultyTextView = findViewById(R.id.difficulty_text_view)
//
//        difficultyTextView.text = DifficultyUtils().getName(context, viewModel.getCurrentDifficultyName())
//
//        // При нажатии на кнопку, уровень сложности игры уменьшается с помощью метода decrementDifficulty() из viewModel. Здесь также создается анимация slide_right
//        decrementButton.setOnClickListener {
//            viewModel.decrementDifficulty()
//            // Загрузка сохраненной игры
//            viewModel.loadGame()
//            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_right)
//            difficultyTextView.startAnimation(animation)
//        }
//
//        // При нажатии на кнопку, уровень сложности игры увеличивается с помощью метода incrementDifficulty() из viewModel. Здесь также создается анимация slide_left
//        incrementButton.setOnClickListener {
//            viewModel.incrementDifficulty()
//            // Загрузка сохраненной игры
//            viewModel.loadGame()
//            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)
//            difficultyTextView.startAnimation(animation)
//        }
//
//        viewModel.difficultyLiveData.observe(viewLifecycleOwner) {
//            binding.difficultyTextView.text = DifficultyUtils().getName(requireContext(), it)
//        }
//    }
//
//    fun setViewModel(viewModel: DifficultyViewModel, viewLifecycleOwner: LifecycleOwner) {
//        // Вставьте ваш код для наблюдения за LiveData и обновления TextView
//
//        difficultyLiveData.observe(viewLifecycleOwner) {
//
//        }
//
//        this.viewModel = viewModel
//    }
//}