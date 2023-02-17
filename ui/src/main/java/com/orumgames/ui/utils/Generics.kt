package com.orumgames.ui.utils

inline val <reified T> T.TAG: String
    get() = T::class.java.canonicalName ?: T::class.simpleName ?: T::class.java.simpleName

typealias OnItemClicked<T> = (position: Int, data: T) -> Unit