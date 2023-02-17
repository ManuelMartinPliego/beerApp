package com.orumgames.ui.home

import android.text.TextWatcher
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.orumgames.domain.common.usecase.flow.collectInLifeCycle
import com.orumgames.domain.models.Beer
import com.orumgames.ui.R
import com.orumgames.ui.base.BaseFragment
import com.orumgames.ui.callbacks.HomeCallbacks
import com.orumgames.ui.databinding.FragmentHomeBinding
import com.orumgames.ui.utils.LoadingFragment
import com.orumgames.ui.utils.loadImage
import com.orumgames.ui.utils.onBackPressedNavigateToHome
import com.orumgames.ui.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val mViewModel: HomeViewModel by activityViewModels()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
    private lateinit var loadingFragment: LoadingFragment
    private var listener: HomeCallbacks? = null
    private var textWatcher: TextWatcher? = null
    private val homeAdapter by lazy {
        HomeAdapter().apply {
            onClick = { openDetailBeer(it) }
        }
    }

    override fun setupViews() {
        with(mViewBinding) {
            with(rvListBeers) {
                adapter = homeAdapter
                setHasFixedSize(true)
            }
        }
        changeImageToolbar()
        loadingFragment = LoadingFragment(requireActivity())
        super.setupViews()
    }

    override fun attachListeners() {
        with(mViewBinding) {
            rvListBeers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(HomeViewModel.PAGE_THRESHOLD))
                        mViewModel.viewAllBeers()
                }
            })
        }
        startSearch()
        onBackPressedNavigateToHome(requireActivity(), listener)
        super.attachListeners()
    }

    override fun setupViewModel() {
        mViewModel.viewAllBeers()
    }

    override fun attachObservers() {
        with(mViewModel) {
            beer.observe(viewLifecycleOwner) {
                homeAdapter.setData(it)
            }
            states.collectInLifeCycle(viewLifecycleOwner) {
                renderStates(it)
            }
        }
    }

    private fun renderStates(state: HomeViewModel.State) {
        when (state) {
            is HomeViewModel.State.Loading -> {
                when (state.isLoading) {
                    true -> loadingFragment.startLoadingDialog()
                    false -> loadingFragment.dismissDialog()
                }
            }
            is HomeViewModel.State.OnSuccess -> {
                loadingFragment.dismissDialog()
            }
            is HomeViewModel.State.OnError -> {
                loadingFragment.dismissDialog()
            }
        }
    }

    private fun changeImageToolbar() {
        with(mViewBinding) {
            viewToolbarHomeSection.imgSearch.visible()
            viewToolbarHomeSection.imgSearch.loadImage(R.drawable.ic_search)
        }
    }

    private fun openDetailBeer(beer: Beer?) {
        beer?.let {
            findNavController()
                .navigate(HomeFragmentDirections.homeToNavigationDetailBeer(beer.id.toString()))
        }
    }

    private fun startSearch() {
        with(mViewBinding) {
            textWatcher =
                viewToolbarHomeSection.edSearch.doOnTextChanged { text, _, _, _ ->
                    homeAdapter.filter.filter(text)
                }
        }
    }
}