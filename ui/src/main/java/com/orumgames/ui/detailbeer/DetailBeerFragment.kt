package com.orumgames.ui.detailbeer

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.orumgames.domain.common.usecase.flow.collectInLifeCycle
import com.orumgames.domain.models.Beer
import com.orumgames.ui.base.BaseFragment
import com.orumgames.ui.databinding.FragmentDetailBeerBinding
import com.orumgames.ui.utils.LoadingFragment
import com.orumgames.ui.utils.loadImageUrl
import com.orumgames.ui.utils.onBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBeerFragment : BaseFragment<FragmentDetailBeerBinding, DetailBeerViewModel>() {

    override val mViewModel: DetailBeerViewModel by viewModels()
    override fun getViewBinding(): FragmentDetailBeerBinding = FragmentDetailBeerBinding.inflate(layoutInflater)
    private lateinit var loadingFragment: LoadingFragment

    override fun setupViews() {
        loadingFragment = LoadingFragment(requireActivity())
        validateArguments()
    }

    override fun attachListeners() {
        with(mViewBinding) {
            imgBackToolbar.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        onBackPressed(this, requireActivity())
        super.attachListeners()
    }

    override fun attachObservers() {
        with(mViewModel) {
            states.collectInLifeCycle(viewLifecycleOwner) {
                renderStates(it)
            }
        }
    }

    private fun validateArguments() {
        arguments?.let { bundle ->
            mViewModel.findBeerById(
                mViewModel.idBeer ?: DetailBeerFragmentArgs.fromBundle(
                    bundle
                ).id
            )
        }
    }

    private fun viewDataBeer(beer: Beer) {
        with(mViewBinding) {
            tvNameBeer.text = beer.name
            tvDescriptionBeer.text = beer.description
            tvAbvBeer.text = beer.abv.toString()
            beer.imageUrl?.let {
                imgPublication.loadImageUrl(beer.imageUrl)
            }
        }
    }

    private fun renderStates(state: State) {
        when(state) {
            is State.Loading -> {
                when (state.isLoading) {
                    true -> loadingFragment.startLoadingDialog()
                    false -> loadingFragment.dismissDialog()
                }
            }
            is State.BeerReady -> {
                loadingFragment.dismissDialog()
                viewDataBeer(state.beer)
            }
            is State.Error -> findNavController().popBackStack()
        }
    }
}