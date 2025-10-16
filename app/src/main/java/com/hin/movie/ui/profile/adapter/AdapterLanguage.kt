package com.hin.movie.ui.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.data.entities.OptionItem
import com.hin.movie.databinding.ItemRadioOptionBinding
import timber.log.Timber

class AdapterLanguage : ListAdapter<OptionItem, AdapterLanguage.ViewHolder>(diffCallback) {
    private var onItemClickListener: ((OptionItem) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemRadioOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OptionItem) {
            binding.radioAuto.text = item.title
            binding.radioAuto.isChecked = item.isSelected

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    fun setOnItemClickListener(listener: (OptionItem) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<OptionItem>() {
            override fun areItemsTheSame(
                oldItem: OptionItem,
                newItem: OptionItem
            ): Boolean {
                return oldItem.value == newItem.value
            }

            override fun areContentsTheSame(
                oldItem: OptionItem,
                newItem: OptionItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterLanguage.ViewHolder {
        val binding = ItemRadioOptionBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterLanguage.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}