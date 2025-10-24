package com.hin.movie.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.data.entities.MovieWithEpisodeJoin
import com.hin.movie.databinding.ItemMovieHistoryBinding
import com.hin.movie.utils.extensions.toTimeFormat

class AdapterHistory : ListAdapter<MovieWithEpisodeJoin, AdapterHistory.ViewHolder>(DIFF_CALLBACK) {
    private var itemBookmarkListener: ItemHistoryListener? = null
    private var movies: List<MovieWithEpisodeJoin>? = null
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieWithEpisodeJoin>() {
            override fun areItemsTheSame(
                oldItem: MovieWithEpisodeJoin,
                newItem: MovieWithEpisodeJoin
            ): Boolean {
                return oldItem.movie.slug == newItem.movie.slug
            }

            override fun areContentsTheSame(
                oldItem: MovieWithEpisodeJoin,
                newItem: MovieWithEpisodeJoin
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemMovieHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieWithEpisodeJoin) {
            binding.name = item.movie.movie.name
            binding.imageUrl = item.movie.movie.thumbImageUrl
            binding.currentEpisode = item.episode.episode.name
            binding.currentTime = item.episode.currentPositionEpisode.toTimeFormat()

            binding.root.setOnClickListener {
                itemBookmarkListener?.onClick(item)
            }

            binding.btnDelete.setOnClickListener {
                itemBookmarkListener?.onClickDelete(item)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHistory.ViewHolder {
        val binding =
            ItemMovieHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterHistory.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItemBookmarkListener(listener: ItemHistoryListener) {
        itemBookmarkListener = listener
    }

    fun setSublistMovie(movies: List<MovieWithEpisodeJoin>) {
        this.movies = movies
        submitList(movies)
    }

    fun filter(query: String){
        val filteredList = this.movies?.filter { item ->
            item.movie.movie.name?.trim()?.contains(query, ignoreCase = true) ?: false
        }

        submitList(filteredList)
    }
}

interface ItemHistoryListener {
    fun onClick(item: MovieWithEpisodeJoin)
    fun onClickDelete(item: MovieWithEpisodeJoin)
}