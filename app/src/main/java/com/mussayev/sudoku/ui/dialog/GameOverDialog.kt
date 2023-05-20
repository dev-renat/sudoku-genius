package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R
import com.mussayev.sudoku.utils.StringUtils

class GameOverDialog : DialogFragment() {
    private var listener: GameOverDialogListener? = null

    fun setListener(listener: GameOverDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_game_over, null, false)

        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        cancelButton.setOnClickListener {
            listener?.gameOverDialogOnClickCancel()
            dismiss()
        }

        val builder = Dialog(requireActivity())
        builder.setContentView(view)
        builder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)

        return builder
    }
}