package com.hin.movie.ui.bookmark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.data.entities.Movie
import com.hin.movie.databinding.ItemMovieBinding

class AdapterBookmark: ListAdapter<Movie, AdapterBookmark.ViewHolder>(DIFF_CALLBACK) {
    private var itemBookmarkListener: ItemBookmarkListener? = null

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
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
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Movie){
            binding.name = item.name
            binding.imageUrl = item.posterImageUrl
            binding.episode = item.episodeCurrent

            binding.root.setOnClickListener {
                itemBookmarkListener?.onClick(item)
            }

            val tranName = "image_${item.posterImageUrl.hashCode()}"
            ViewCompat.setTransitionName(binding.imageView, tranName)

            val params = binding.root.layoutParams
            val paramsCard = binding.cardItem.layoutParams
            paramsCard.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.root.layoutParams = params
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterBookmark.ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterBookmark.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItemBookmarkListener(listener: ItemBookmarkListener) {
        itemBookmarkListener = listener
    }
}

interface ItemBookmarkListener {
    fun onClick(item: Movie)
}