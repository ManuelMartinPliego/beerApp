package com.orumgames.ui.loading

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingFragmentManager @Inject constructor() {

    private var job: Job? = null

    fun showLoading(
        fragment: Fragment,
        label: CharSequence? = null,
        delay: Int = LOADING_FRAGMENT_DELAY,
        dialog: DialogFragment? = null
    ) {
        createJob(fragment, delay, dialog ?: LoadingDialog.newInstance(label))
    }

    private fun createJob(fragment: Fragment, delay: Int, dialogFragment: DialogFragment) {
        job?.cancel()
        job = fragment.lifecycleScope.launchWhenCreated { showLoading(fragment, dialogFragment, delay) }
    }

    private suspend fun showLoading(fragment: Fragment, dialog: DialogFragment, delay: Int) {
        delay(delay.toLong())
        if (!hasVisibleDialog(fragment)) {
            try {
                if (job?.isActive != true) return
                dialog.showNow(fragment.childFragmentManager, LOADING_FRAGMENT_TAG)
            } catch (e: Exception) {
                Log.d(TAG, "showLoading: ${e.stackTraceToString()}")
            }
        }
    }

    private fun hasVisibleDialog(fragment: Fragment): Boolean {
        if (fragment.activity != null && fragment.isAdded && fragment.childFragmentManager.fragments.isNotEmpty()) {
            return fragment.childFragmentManager.fragments.any { it is DialogFragment && fragment.isVisible }
        }
        return false
    }

    fun hideLoading(
        fragment: Fragment,
        delay: Int = HIDE_LOADING_FRAGMENT_DELAY,
    ) {
        job?.cancel()
        fragment.lifecycleScope.launchWhenCreated { hideDialog(fragment, LOADING_FRAGMENT_TAG, delay) }
    }

    private suspend fun hideDialog(fragment: Fragment, tag: String = LOADING_FRAGMENT_TAG, delay: Int) {
        try {
            delay(delay.toLong())
            (fragment.childFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismissAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
    }

    fun isLoadingVisible(fragment: Fragment): Boolean {
        return fragment.childFragmentManager.findFragmentByTag(LOADING_FRAGMENT_TAG) != null
    }

    companion object {
        const val HIDE_LOADING_FRAGMENT_DELAY = 0
        const val LOADING_FRAGMENT_DELAY = 150
        const val LOADING_FRAGMENT_TAG = "LOADING_FRAGMENT_TAG"
        const val TAG = "LoadingFragmentManager"
    }
}