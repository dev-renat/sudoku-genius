package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R

class AuthDialog : DialogFragment() {
    private var listener: AuthDialogListener? = null

    fun setListener(listener: AuthDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_auth, null, false)

        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        cancelButton.setOnClickListener {
            listener?.authDialogOnClickCancel()
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