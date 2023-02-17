package com.orumgames.ui.common

import android.content.Context
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.MainThread

object KeyboardUtils {

    private val LOG_TAG = KeyboardUtils::class.java.simpleName

    @MainThread
    @JvmStatic // For calling from Java
    fun showKeyBoard(context: Context, myEditText: EditText) {
        try {
            myEditText.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(myEditText, 0)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
        }
    }

    @MainThread
    @JvmStatic // For calling from Java
    fun hideKeyBoard(context: Context, windowToken: IBinder?) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
        }
    }

    @MainThread
    @JvmStatic // For calling from Java
    fun hideKeyBoard(context: Context, myEditText: EditText?) {
        if (myEditText != null) {
            hideKeyBoard(context, myEditText.windowToken)
        }
    }

}