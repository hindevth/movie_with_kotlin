package com.hin.flixcomix.ui.watch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.data.entities.Server
import com.hin.flixcomix.databinding.ItemEpisodeBinding

class AdapterServer : ListAdapter<Server, AdapterServer.ViewHolder>(DIFF_CALL_BACK) {
    private var onItemClickListener: ((Server) -> Unit)? = null
    private var serverSelected: Server? = null

    fun setOnItemClickListener(listener: (Server) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemEpisode(server: Server?) {
        val oldSelected = serverSelected
        serverSelected = server

        oldSelected?.let {
            val index = currentList.indexOf(it)
            if (index != -1) {
                notifyItemChanged(index)
            }
        }

        serverSelected?.let {
            val index = currentList.indexOf(it)
            if (index != -1) {
                notifyItemChanged(index)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Server>() {
            override fun areItemsTheSame(oldItem: Server, newItem: Server): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Server, newItem: Server): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Server) {
            binding.txtName.text = item.name
        }
    }


}