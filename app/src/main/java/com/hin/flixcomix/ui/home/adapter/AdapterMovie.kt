package com.hin.flixcomix.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.ItemMovieBinding
import com.hin.flixcomix.utils.extensions.dpToPx

class AdapterMovie : ListAdapter<Movie, AdapterMovie.ViewHolder>(MovieDiffCallback()) {
    private var onItemListener: OnItemListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.name = item.name
            binding.episode = item.episodeCurrent
            binding.imageUrl = item.posterImageUrl

            val tranName = "image_${item.posterImageUrl.hashCode()}"
            ViewCompat.setTransitionName(binding.imageView, tranName)

            val params = binding.root.layoutParams
            params.width = 150.dpToPx(binding.root.context)
            binding.root.layoutParams = params


            binding.root.setOnClickListener {
                onItemListener?.onClick(binding.imageView, item, tranName)
            }
        }
    }

    fun setOnClickItem(onClickItem: OnItemListener) {
        this.onItemListener = onClickItem
    }
}

interface OnItemListener {
    fun onClick(imageView: ImageView, item: Movie, transitionName: String)
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }

}