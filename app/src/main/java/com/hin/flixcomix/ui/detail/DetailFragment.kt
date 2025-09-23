package com.hin.flixcomix.ui.detail

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.FragmentDetailBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.detail.adapter.AdapterPaperSuggest
import com.hin.flixcomix.utils.extensions.getParcelableCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val viewModel: DetailViewModel by viewModels()
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val movie = arguments?.getParcelableCompat<Movie>("movie")
        val transName = arguments?.getString("transName")

        ViewCompat.setTransitionName(binding.imageMovie, transName)
        viewModel.setMovie(movie)
        viewModel.loadMovieDetail(movie?.slug)



        binding.viewPaper.adapter =
            AdapterPaperSuggest(this, movie?.category?.random()?.slug, movie?.slug.toString())
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPaper) { tab, position ->
                tab.text = when (position) {
                    0 -> "More Like This"
                    else -> "Comments"
                }
            }.apply { attach() }
    }

    fun onClick() {
        binding.btnBack.setOnClickListener { popNavigate() }
        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.btnPlay.setOnClickListener {
            navigateTo(R.id.action_watchFragment, Bundle().apply {
                viewModel.movie.value.let { movie ->
                    putParcelable("movie", movie)
                }
                viewModel.servers.value?.let { servers ->
                    putParcelableArrayList("servers", ArrayList(servers))
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }
}