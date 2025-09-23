package com.hin.flixcomix.ui.grid_suggest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.ItemMovieBinding
import com.hin.flixcomix.ui.home.adapter.OnItemListener
import com.hin.flixcomix.utils.extensions.dpToPx
import kotlin.text.toInt
import kotlin.times

class AdapterGridSuggest :
    ListAdapter<Movie, AdapterGridSuggest.GridViewHolder>(GridSuggestDiffCallBack()) {
    private var onItemListener: OnItemListener? = null

    inner class GridViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.name = movie.name
            binding.imageUrl = movie.posterImageUrl
            binding.episode = movie.episodeCurrent

            val params = binding.root.layoutParams
            params.height = 245.dpToPx(binding.root.context)
            binding.root.layoutParams = params

            binding.root.setOnClickListener {
                onItemListener?.onClick(
                    binding.imageView,
                    movie,
                    "image_${movie.posterImageUrl.hashCode()}"
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemListener(onItemListener: OnItemListener) {
        this.onItemListener = onItemListener
    }
}

class GridSuggestDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.slug == newItem.slug
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }

}