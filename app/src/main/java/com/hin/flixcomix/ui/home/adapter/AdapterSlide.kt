package com.hin.flixcomix.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.ItemSlideBinding

class AdapterSlide : ListAdapter<Movie, AdapterSlide.SlideViewHolder>(SlideDiffCallback()) {
    private var onPlayClick: OnPlayItemListener? = null
    private var onMyListClick: OnPlayMyListListener? = null

    inner class SlideViewHolder(private val binding: ItemSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.name = item.name
            binding.categories = item.category?.joinToString { it.name.toString() }
            binding.imageUrl = item.posterImageUrl

            val tranName = "image_${item.posterImageUrl.hashCode()}"
            ViewCompat.setTransitionName(binding.imageView, tranName)

            binding.btnPlay.setOnClickListener {
                onPlayClick?.onClick(item, tranName)
            }

            binding.btnMyList.setOnClickListener {
                onMyListClick?.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideViewHolder {
        val binding = ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnPlayClick(onClick: OnPlayItemListener) {
        this.onPlayClick = onClick
    }

    fun setOnMyListClick(onClick: OnPlayMyListListener) {
        this.onMyListClick = onClick
    }
}

interface OnPlayItemListener {
    fun onClick(item: Movie, transName: String)
}

interface OnPlayMyListListener {
    fun onClick(item: Movie)
}

class SlideDiffCallback : DiffUtil.ItemCallback<Movie>() {
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