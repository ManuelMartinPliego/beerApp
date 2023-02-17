package com.orumgames.ui.extensions

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.orumgames.ui.delegates.FragmentViewBindingDelegate

inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)