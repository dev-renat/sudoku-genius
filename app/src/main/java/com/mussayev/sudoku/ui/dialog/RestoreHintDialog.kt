package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R

class RestoreHintDialog : DialogFragment() {
    private var listener: RestoreHintDialogListener? = null

    fun setListener(listener: RestoreHintDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_restore_hint, null, false)

        val okButton = view.findViewById(R.id.dialog_ok_button) as AppCompatButton
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        okButton.setOnClickListener {
            listener?.restoreHintDialogOnClickOk()
            dismiss()
        }
        cancelButton.setOnClickListener {
            listener?.restoreHintDialogOnClickCancel()
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