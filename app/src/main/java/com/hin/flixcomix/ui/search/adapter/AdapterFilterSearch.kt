package com.hin.flixcomix.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.ItemFilterBinding

class AdapterFilterSearch :
    ListAdapter<String, AdapterFilterSearch.FilterSearchViewHolder>(FilterDiffCallback()) {
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterSearchViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterSearchViewHolder, position: Int) {
        val pos = holder.bindingAdapterPosition
        holder.bind(getItem(position), pos == selectedPosition)

        holder.itemView.setOnClickListener {
            val prevPosition = selectedPosition
            selectedPosition = pos
            if (prevPosition == selectedPosition){
                selectedPosition = RecyclerView.NO_POSITION
            }
            notifyItemChanged(prevPosition)
            notifyItemChanged(position)
        }
    }

    fun getItem(): String? {
        return getItem(selectedPosition)
    }

    inner class FilterSearchViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, isSelected: Boolean) {
            binding.btnFilter.text = item
            toggleButton(isSelected)
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
}

class FilterDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

}