package com.hin.movie.ui.custom.exo_player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.databinding.ItemTimerBinding
import com.hin.movie.ui.custom.exo_player.data.Timer
import com.hin.movie.utils.extensions.visible

class AdapterTimer : androidx.recyclerview.widget.ListAdapter<Timer, AdapterTimer.ViewHolder>(DiffCallback) {
    private var listener: OnListenerItemTimer? = null
    private var positionSelected: Timer? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    fun setSelectedItem(item: Timer?) {
        val oldSelected = positionSelected
        positionSelected = item

        oldSelected.let {
            val oldIndex = currentList.indexOf(it)
            if (oldIndex != -1) notifyItemChanged(oldIndex)
        }
        val newIndex = currentList.indexOf(item)
        if (newIndex != -1) notifyItemChanged(newIndex)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Timer>() {
            override fun areItemsTheSame(
                oldItem: Timer,
                newItem: Timer
            ): Boolean {
                return oldItem.value == newItem.value
            }

            override fun areContentsTheSame(
                oldItem: Timer,
                newItem: Timer
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemTimerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Timer) {
            binding.txtItem.text = item.name

            binding.root.setOnClickListener {
                listener?.onClick(item)
                setSelectedItem(item)
            }

            if (positionSelected?.value == item.value) {
                binding.imgChecked.visible()
            } else {
                binding.imgChecked.invalidate()
            }
        }
    }

    fun setOnListener(listener: OnListenerItemTimer) {
        this.listener = listener
    }
}

interface OnListenerItemTimer {
    fun onClick(item: Timer)
}