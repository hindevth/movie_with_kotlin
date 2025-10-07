package com.hin.flixcomix.ui.custom.exo_player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.databinding.ItemSpeedBinding
import com.hin.flixcomix.ui.custom.exo_player.data.Speed

class AdapterSpeed : androidx.recyclerview.widget.ListAdapter<Speed, AdapterSpeed.ViewHolderSpeed>(DIFF_CALLBACK) {
    private var listener: OnListenerItemSpeed? = null

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Speed>() {
            override fun areItemsTheSame(oldItem: Speed, newItem: Speed): Boolean {
                return oldItem.value == newItem.value
            }

            override fun areContentsTheSame(
                oldItem: Speed,
                newItem: Speed
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderSpeed {
        val binding = ItemSpeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderSpeed(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolderSpeed,
        position: Int
    ) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolderSpeed(private val binding: ItemSpeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Speed) {
            binding.btnItemSpeed.text = item.name
            binding.btnItemSpeed.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    fun setListener(listener: OnListenerItemSpeed) {
        this.listener = listener
    }
}

interface OnListenerItemSpeed {
    fun onClick(item: Speed)
}