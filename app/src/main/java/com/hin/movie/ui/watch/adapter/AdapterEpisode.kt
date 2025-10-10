package com.hin.movie.ui.watch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.R
import com.hin.movie.data.entities.Episode
import com.hin.movie.databinding.ItemEpisodeBinding

class AdapterEpisode : ListAdapter<Episode, AdapterEpisode.ViewHoler>(DIFF_CALLBACK) {
    private var selectedPosition = RecyclerView.NO_POSITION
    private var listener: EpisodeListener? = null

    fun setListener(listener: EpisodeListener) {
        this.listener = listener
    }

    fun setSelectedItem(position: Int){
        val prevPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(prevPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class ViewHoler(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val prevPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

                listener?.onClick(getItem(selectedPosition), selectedPosition)

                notifyItemChanged(prevPosition)
                notifyItemChanged(selectedPosition)
            }
        }

        fun bind(item: Episode) {
            binding.txtName.text = item.name

            if (selectedPosition != bindingAdapterPosition) {
                binding.txtName.background = AppCompatResources.getDrawable(binding.txtName.context,R.drawable.bg_outline_squere)
            } else {
                binding.txtName.background = AppCompatResources.getDrawable(binding.txtName.context,R.drawable.bg_squere)
                binding.txtName.setTextColor(ContextCompat.getColor(binding.txtName.context,R.color.white))
            }
        }
    }

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
}

interface EpisodeListener {
    fun onClick(item: Episode, position: Int)
}