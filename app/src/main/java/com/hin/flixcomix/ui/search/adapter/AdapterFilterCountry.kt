package com.hin.flixcomix.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.databinding.ItemFilterBinding

class AdapterFilterCountry :
    ListAdapter<Country, AdapterFilterCountry.FilterSearchViewHolder>(FilterCountryDiffCallback()) {
    private var selected: Country? = null
    private var onListener: AdapterFilterCountryListener? = null

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

    inner class FilterSearchViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Country) {
            binding.btnFilter.text = item.name
            toggleButton(selected?.slug == item.slug)

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

    fun setSelectedItem(item: Country?) {
        val oldSelected = selected
        selected = item

        oldSelected?.let {
            val oldIndex = currentList.indexOf(it)
            if (oldIndex != -1) notifyItemChanged(oldIndex)
        }
        val newIndex = currentList.indexOf(item)
        if (newIndex != -1) notifyItemChanged(newIndex)
    }

    fun setOnListener(onListener: AdapterFilterCountryListener){
        this.onListener = onListener
    }
}

interface AdapterFilterCountryListener{
    fun onClick(item: Country)
}


class FilterCountryDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Country,
        newItem: Country
    ): Boolean {
        return oldItem == newItem
    }

}