package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R

class ConfirmExitDialog(
    private val title: String,
    private val text: String,
    private var close: Boolean = true
) : DialogFragment() {
    private var listener: ConfirmExitDialogListener? = null

//    private var title: String = ""
//    private var text: String = ""
//
//    companion object {
//        const val ARG_TITLE = "title"
//        const val ARG_TEXT = "text"
//
//        fun newInstance(param1: String, param2: String): EndgameRatingDialog {
//            val args = Bundle()
//            args.putString(ARG_TITLE, param1)
//            args.putString(ARG_TEXT, param2)
//
//            val fragment = EndgameRatingDialog()
//            fragment.arguments = args
//            return fragment
//        }
//    }

    fun setListener(listener: ConfirmExitDialogListener) {
        this.listener = listener
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        title = arguments?.getString(ARG_TITLE) ?: ""
//        text = arguments?.getString(ARG_TEXT) ?: ""
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_confirm_exit, null, false)

        val titleTextView = view.findViewById(R.id.title) as TextView
        val textTextView = view.findViewById(R.id.text) as TextView
        val okButton = view.findViewById(R.id.dialog_ok_button) as AppCompatButton
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        titleTextView.text = title
        textTextView.text = text

        okButton.setOnClickListener {
            listener?.confirmExitDialogOnClickOk()
            if (close) dismiss()
        }

        cancelButton.setOnClickListener {
            listener?.confirmExitDialogOnClickCancel()
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