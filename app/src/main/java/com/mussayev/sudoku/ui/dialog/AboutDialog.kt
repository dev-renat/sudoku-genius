package com.mussayev.sudoku.ui.dialog

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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

class AboutDialog : DialogFragment() {
    private var listener: AboutDialogListener? = null
    private var versionName: String? = null

    fun setListener(listener: AboutDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val packageManager = context?.packageManager
        val packageName = context?.packageName
        packageName?.let {
            val packageInfo = packageManager?.getPackageInfo(packageName, 0)
            versionName = packageInfo?.versionName
        }

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_about, null, false)

        val versionNameTextView = view.findViewById(R.id.versionName) as TextView
        val privacyPolicy = view.findViewById(R.id.privacy_policy) as TextView
        val twitter = view.findViewById(R.id.twitter) as ImageButton
        val telegram = view.findViewById(R.id.telegram) as ImageButton
        val instagram = view.findViewById(R.id.instagram) as ImageButton
        val goToGooglePlay = view.findViewById(R.id.go_to_google_play) as TextView
        val cancelButton = view.findViewById(R.id.dialog_cancel_button) as AppCompatButton

        versionNameTextView.text = getString(R.string.version_name, versionName)

        privacyPolicy.setOnClickListener {
            StringUtils.link(requireContext(), "https://mussayev.com/sudoku/privacy", "Privacy Policy")
        }

        twitter.setOnClickListener {
            StringUtils.link(requireContext(), "https://twitter.com/dev_renat", "Twitter")
        }

        telegram.setOnClickListener {
            StringUtils.link(requireContext(), "https://t.me/sudoku_genius", "Telegram")
        }

        instagram.setOnClickListener {
            StringUtils.link(requireContext(), "https://www.instagram.com/dev_renat/", "Instagram")
        }

        //
        goToGooglePlay.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context?.packageName}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")))
            }
        }

        cancelButton.setOnClickListener {
            listener?.aboutDialogOnClickCancel()
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