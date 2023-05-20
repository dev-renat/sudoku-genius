package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R

class ResetGameDialog : DialogFragment() {
    private var listener: ResetGameDialogListener? = null

    fun setListener(listener: ResetGameDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_reset_game, null, false)

        val okButton = view.findViewById(R.id.dialog_ok_button) as AppCompatButton
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        okButton.setOnClickListener {
            listener?.resetGameDialogOnClickOk()
            dismiss()
        }
        cancelButton.setOnClickListener {
            listener?.resetGameDialogOnClickCancel()
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