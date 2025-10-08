package com.hin.flixcomix.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.databinding.ItemFilterBinding

class AdapterFilterGenre :
    ListAdapter<Genre, AdapterFilterGenre.FilterSearchViewHolder>(FilterGenreDiffCallback()) {
    private var selected: Genre? = null
    private var onListener: AdapterFilterGenreListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterSearchViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterSearchViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    fun setSelectedItem(item: Genre?) {
        val oldSelected = selected
        selected = item

        oldSelected?.let {
            val oldIndex = currentList.indexOf(it)
            if (oldIndex != -1) notifyItemChanged(oldIndex)
        }
        val newIndex = currentList.indexOf(item)
        if (newIndex != -1) notifyItemChanged(newIndex)
    }

    inner class FilterSearchViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Genre) {
            binding.btnFilter.text = item.name
            toggleButton(selected?.slug == item.slug)
            val lp = binding.btnFilter.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams){
                lp.flexGrow = 1.0f
            }
            binding.btnFilter.setOnClickListener {
                onListener?.onClick(item)
            }
        }

        private fun toggleButton(isSelected: Boolean) {
            if (isSelected) {
                binding.btnFilter.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                binding.btnFilter.setBackgroundResource(R.drawable.button_bg)
            } else {
                binding.btnFilter.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.orange
                    )
                )
                binding.btnFilter.setBackgroundResource(R.drawable.color_outline_orange)
            }

        }
    }

    fun setOnListener(onListener: AdapterFilterGenreListener) {
        this.onListener = onListener
    }
}

interface AdapterFilterGenreListener {
    fun onClick(item: Genre)
}


class FilterGenreDiffCallback : DiffUtil.ItemCallback<Genre>() {
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Genre,
        newItem: Genre
    ): Boolean {
        return oldItem == newItem
    }

}