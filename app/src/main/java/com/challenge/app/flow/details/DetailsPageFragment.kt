package com.challenge.app.flow.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.challenge.app.R
import com.challenge.app.base.BaseFragment
import com.challenge.app.databinding.FragmentDetailsPageBinding
import com.challenge.app.databinding.ListItemMaltBinding
import com.challenge.app.extensions.makeGone
import org.koin.android.ext.android.inject
import androidx.activity.OnBackPressedCallback
import com.challenge.app.extensions.setNavigationResult


class DetailsPageFragment : BaseFragment() {

    companion object {
        const val REQ_KEY_BEER = "REQ_KEY_BEER"
    }

    private val viewModel by inject<DetailsPageViewModel>()

    private val args: DetailsPageFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initFavorite(args.beer.isFavorite)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateUp()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews(rootView: View) = with(binding) {
        ivIconBack.setOnClickListener {
            navigateUp()
        }

        ivFavorite.setOnClickListener {
            viewModel.toggleFavorite(args.beer.id.toString())
        }

        viewModel.favoriteLiveData.observe(viewLifecycleOwner) {
            ivFavorite.setImageResource(viewModel.getFavoriteImage(it))
        }

        args.beer.let { item ->
            Glide.with(ivLogo)
                .load(item.imageUrl)
                .centerInside()
                .placeholder(R.drawable.bg_image_place_holder)
                .into(ivLogo)

            tvToolbar.text = item.name
            tvName.text = item.name

            tvDescription.text = item.description
            tvABV.text = resources.getString(R.string.abv, item.abv ?: "")

            item.malt?.forEach { malt ->
                lytMalt.addView(
                    ListItemMaltBinding.inflate(LayoutInflater.from(context)).apply {
                        tv.text = resources.getString(
                            R.string.malt_item,
                            malt.name,
                            malt.amount.value,
                            malt.amount.unit
                        )
                    }.root
                )
            } ?: run {
                tvMaltTitle.makeGone()
                lytMalt.makeGone()
            }

            item.hops?.forEach { hop ->
                lytHops.addView(
                    ListItemMaltBinding.inflate(LayoutInflater.from(context)).apply {
                        tv.text = resources.getString(
                            R.string.hop_item,
                            hop.name,
                            hop.amount.value,
                            hop.amount.unit,
                            hop.add,
                            hop.attribute
                        )
                    }.root
                )
            } ?: run {
                tvHopsTitle.makeGone()
                lytHops.makeGone()
            }


            item.foodPairing?.forEach { food ->
                lytFoodPairings.addView(
                    ListItemMaltBinding.inflate(LayoutInflater.from(context)).apply {
                        tv.text = food
                    }.root
                )
            } ?: run {
                tvFoodsPairingTitle.makeGone()
                lytFoodPairings.makeGone()
            }
        }
    }

    private fun navigateUp() {
        setNavigationResult(
            REQ_KEY_BEER,
            Pair(
                args.beer.id,
                viewModel.favoriteLiveData.value
            )
        )
        findNavController().navigateUp()
    }
}
