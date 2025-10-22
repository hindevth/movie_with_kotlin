package com.hin.movie.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hin.movie.R
import com.hin.movie.data.entities.Movie
import com.hin.movie.databinding.FragmentDetailBinding
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.detail.adapter.AdapterPaperSuggest
import com.hin.movie.utils.extensions.getParcelableCompat
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
                    0 -> ContextCompat.getString(requireContext(),R.string.more_like_this)
                    else -> ContextCompat.getString(requireContext(),R.string.comments)
                }
            }.apply { attach() }

        viewModel.isBookmark.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnBookmark.setImageResource(R.drawable.bookmark_bold)
            } else {
                binding.btnBookmark.setImageResource(R.drawable.bookmark)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            (activity as? MainActivity)?.showLoading(it)
        }
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

        binding.btnBookmark.setOnClickListener {
            viewModel.toggleBookmark()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }
}