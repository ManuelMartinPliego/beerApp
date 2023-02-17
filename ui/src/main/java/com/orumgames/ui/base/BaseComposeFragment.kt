package com.orumgames.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.orumgames.ui.loading.LoadingFragmentManager
import javax.inject.Inject

abstract class BaseComposeFragment<VModel: BaseViewModel> : Fragment(), ComposeFragmentDelegate {

    protected abstract val mViewModel: VModel
    @Inject
    protected lateinit var loadingManager: LoadingFragmentManager
    protected val mTag: String = this::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        createComposeView(inflater.context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModel()
        attachObservers()
        attachListeners()
    }

    open fun setupViews() {}
    open fun setupViewModel() {}
    open fun attachObservers() {}
    open fun attachListeners() {}
}

interface ComposeFragmentDelegate {
    @Composable
    fun UI()

    fun createComposeView(context: Context) =
        ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { UI() }
        }
}
