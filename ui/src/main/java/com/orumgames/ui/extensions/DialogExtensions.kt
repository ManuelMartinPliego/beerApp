package com.orumgames.ui.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.fragment.app.DialogFragment
import com.orumgames.ui.common.KeyboardUtils

fun DialogFragment.setFullScreenMode() {
    dialog?.window?.apply {
        requestFeature(DialogFragment.STYLE_NO_TITLE)
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        statusBarColor = Color.TRANSPARENT
    }
}

fun DialogFragment.setTransparentBackground() {
    dialog?.window?.apply { setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
}

fun DialogFragment.setLayout(width: Int, height: Int) {
    dialog?.window?.setLayout(width, height)
}

fun Dialog.hideKeyboard() {
    val windowToken = currentFocus?.windowToken ?: return
    KeyboardUtils.hideKeyBoard(context, windowToken)
    currentFocus?.clearFocus()
}