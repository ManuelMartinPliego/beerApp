package com.orumgames.ui.loading

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.orumgames.ui.R
import com.orumgames.ui.databinding.DialogLoadingBinding
import com.orumgames.ui.extensions.hideKeyboard
import com.orumgames.ui.extensions.setFullScreenMode
import com.orumgames.ui.extensions.setTransparentBackground
import com.orumgames.ui.extensions.viewBinding

class LoadingDialog : DialogFragment(R.layout.dialog_loading) {

    companion object {
        private const val KEY_LABEL = "label"

        fun newInstance(label: CharSequence?): LoadingDialog {
            val args =
                Bundle().apply {
                    label?.let { putCharSequence(KEY_LABEL, it) }
                }
            return LoadingDialog().apply { arguments = args }
        }
    }

    private var label: CharSequence? = null
    private val binding by viewBinding<DialogLoadingBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        label = if (savedInstanceState != null) {
            savedInstanceState.getCharSequence(KEY_LABEL)
        } else {
            requireArguments().getCharSequence(KEY_LABEL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setFullScreenMode()
        setTransparentBackground()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            label?.let { loadingLabel.text = it }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            putCharSequence(KEY_LABEL, label)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return object : Dialog(requireActivity(), R.style.FullScreenDialogTheme) {
            override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
                hideKeyboard()
                return super.dispatchTouchEvent(motionEvent)
            }
        }
    }
}