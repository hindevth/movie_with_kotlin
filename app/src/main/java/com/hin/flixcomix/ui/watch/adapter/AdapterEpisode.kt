package com.hin.flixcomix.ui.watch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.databinding.ItemEpisodeBinding

class AdapterEpisode : ListAdapter<Episode, AdapterEpisode.ViewHoler>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHoler {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHoler(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHoler,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(
                oldItem: Episode,
                newItem: Episode
            ): Boolean {
                return oldItem.linkM3u8 == newItem.linkM3u8
            }

            override fun areContentsTheSame(
                oldItem: Episode,
                newItem: Episode
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHoler(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Episode) {
            binding.txtName.text = item.name
        }
    }
}