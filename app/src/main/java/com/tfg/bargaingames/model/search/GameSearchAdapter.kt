package com.tfg.bargaingames.model.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tfg.bargaingames.R
import com.tfg.bargaingames.databinding.ItemGameBinding

class GameSearchAdapter: ListAdapter<GameStore, RecyclerView.ViewHolder>(GameStoreDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gameStore = getItem(position)
        (holder as ViewHolder).run {
            with(binding){
                Nombre.text = gameStore.name

                Precio.text = gameStore.price?.let {
                    (it.final.toFloat() / 100).toString() + " " + it.currency
                } ?: "Sin precio"

                Glide.with(itemView.context)
                    .load(gameStore.tinyImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageGame)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGameBinding.bind(view)
    }

    private class GameStoreDiff : DiffUtil.ItemCallback<GameStore>() {
        override fun areItemsTheSame(oldItem: GameStore, newItem: GameStore): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameStore, newItem: GameStore): Boolean {
            return oldItem == newItem
        }
    }
}
