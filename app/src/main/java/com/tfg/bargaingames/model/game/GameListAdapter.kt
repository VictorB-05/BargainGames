package com.tfg.bargaingames.model.game

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

class GameListAdapter : ListAdapter<Game, RecyclerView.ViewHolder>(GameDiff()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        Log.i("game",game.toString())
        (holder as ViewHolder).run {
            with(binding){
                Nombre.text = game.name
                Precio.text = ""+ (game.finalPrice.toFloat() / 100) + game.currency

                Glide.with(context)
                    .load(game.smallImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageGame)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGameBinding.bind(view)
    }

    private class GameDiff : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }
}