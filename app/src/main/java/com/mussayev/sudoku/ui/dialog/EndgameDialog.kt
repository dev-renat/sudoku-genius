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
import com.mussayev.sudoku.utils.StringUtils

class EndgameDialog(
    private var currentTime: Long = 0,
    private var bestTime: Long = 0
) : DialogFragment() {
    private var listener: EndgameDialogListener? = null

    fun setListener(listener: EndgameDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val currentSeconds = StringUtils.secondsToTime(currentTime)
        var bestSeconds = StringUtils.secondsToTime(bestTime)

        val message = if (currentTime < bestTime && bestTime > 0) {
            resources.getString(R.string.solved_sudoku_message, currentSeconds, bestSeconds)
        } else {
            bestSeconds = if (bestTime == 0L) currentSeconds else bestSeconds
            resources.getString(R.string.solved_sudoku_no_new_record_message, currentSeconds, bestSeconds)
        }

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_endgame, null, false)

        val solvedTimeMessage = view.findViewById(R.id.solved_time_message) as TextView
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        solvedTimeMessage.text = message

        cancelButton.setOnClickListener {
            listener?.endgameDialogOnClickCancel()
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