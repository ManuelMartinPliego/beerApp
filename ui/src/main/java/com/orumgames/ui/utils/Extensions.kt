package com.orumgames.ui.utils

import android.app.AlertDialog
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.orumgames.ui.callbacks.HomeCallbacks

fun View.showMessageSnackBar(@StringRes textId: Int) {
    Snackbar.make(this, textId, Snackbar.LENGTH_LONG).show()
}

fun View.gone() {
    this.visibility = GONE
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun ImageView.loadImage(url: Int) {
    Glide.with(this.context).load(url).into(this)
}

fun ImageView.loadImageUrl(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

fun View.loading(title: String, message: String, buttonPositive: String, buttonNegative: String) {
    val dialog = AlertDialog.Builder(this.context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(buttonPositive) { view, _ ->
            view.dismiss()
        }
        .setNegativeButton(buttonNegative) { view, _ ->
            view.dismiss()
        }
        .setCancelable(false)
        .create()
    dialog.show()
}

fun onBackPressed(fragment: Fragment, activity: FragmentActivity) {
    activity.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController(fragment).popBackStack()
        }
    })
}

fun onBackPressedNavigateToHome(activity: FragmentActivity, listener: HomeCallbacks?, fragmentCallback: (() -> Unit)? = null) {
    activity.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            fragmentCallback?.invoke()
            listener?.selectedHomeMenu()
        }
    })
}

fun <T> merge(first: List<T>?, second: List<T>?): List<T> {
    if(first == null && second == null){
        return emptyList()
    }
    val list: MutableList<T> = ArrayList()
    list.addAll(first ?: emptyList())
    list.addAll(second ?: emptyList())
    return list
}