package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mussayev.sudoku.R
import com.mussayev.sudoku.utils.StringUtils

class RateAppDialog : DialogFragment() {
    private var listener: RateAppDialogListener? = null
    private var versionName: String? = null

    fun setListener(listener: RateAppDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val packageManager = context?.packageManager
        val packageName = context?.packageName
        packageName?.let {
            val packageInfo = packageManager?.getPackageInfo(packageName, 0)
            versionName = packageInfo?.versionName
        }

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_rate_app, null, false)

        val okButton = view.findViewById(R.id.dialog_ok_button) as AppCompatButton
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        okButton.setOnClickListener {
            listener?.rateAppDialogOnClickOk()
            dismiss()
        }

        cancelButton.setOnClickListener {
            listener?.rateAppDialogOnClickCancel()
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