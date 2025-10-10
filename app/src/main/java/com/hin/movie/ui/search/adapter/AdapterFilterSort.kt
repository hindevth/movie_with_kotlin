package com.hin.movie.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.hin.movie.R
import com.hin.movie.data.entities.Sort
import com.hin.movie.databinding.ItemFilterBinding

class AdapterFilterSort :
    ListAdapter<Sort, AdapterFilterSort.FilterSearchViewHolder>(FilterSortDiffCallback()) {
    private var selected: Sort? = null
    private var onListener: AdapterFilterSortListener? = null

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
        fun bind(item: Sort) {
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

    fun setSelectedItem(item: Sort?) {
        val oldSelected = selected
        selected = item

        oldSelected?.let {
            val oldIndex = currentList.indexOf(it)
            if (oldIndex != -1) notifyItemChanged(oldIndex)
        }
        val newIndex = currentList.indexOf(item)
        if (newIndex != -1) notifyItemChanged(newIndex)
    }

    fun setOnListener(onListener: AdapterFilterSortListener){
        this.onListener = onListener
    }
}
interface AdapterFilterSortListener{
    fun onClick(item: Sort)
}

class FilterSortDiffCallback : DiffUtil.ItemCallback<Sort>() {
    override fun areItemsTheSame(oldItem: Sort, newItem: Sort): Boolean {
        return oldItem.slug == newItem.slug
    }

    override fun areContentsTheSame(
        oldItem: Sort,
        newItem: Sort
    ): Boolean {
        return oldItem == newItem
    }

}