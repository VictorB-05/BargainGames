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
import com.tfg.bargaingames.OnClickListener
import com.tfg.bargaingames.R
import com.tfg.bargaingames.databinding.ItemGameBinding
import com.tfg.bargaingames.model.GameItem
import com.tfg.bargaingames.model.database.GameApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameListAdapter : ListAdapter<GameCategorized, RecyclerView.ViewHolder>(GameDiff()) {

    private lateinit var context: Context
    private lateinit var listener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        (holder as ViewHolder).run {
            setListener(game)
            with(binding){
                Nombre.text = game.name
                Precio.text = ""+ (game.finalPrice.toFloat() / 100) + game.currency

                Glide.with(context)
                    .load(game.smallImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageGame)
            }
            CoroutineScope(Dispatchers.IO).launch {
                val gameDb = GameApplication.database.gameDao().getGame(game.id)
                withContext(Dispatchers.Main) {
                    setListener(game)
                    with(binding) {
                        if (gameDb?.favorito == true) {
                            Log.i("game", game.name)
                            cbFavorite.setImageResource(R.drawable.favorite_24)
                        } else {
                            cbFavorite.setImageResource(R.drawable.favorite_no_24)
                        }
                        if (gameDb?.deseado == true) {
                            cbDeseado.setImageResource(R.drawable.add_circle_24)
                        } else {
                            cbDeseado.setImageResource(R.drawable._add_circle_no_24)
                        }
                    }
                }
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGameBinding.bind(view)

        fun setListener(gameItem: GameItem) {
            binding.root.setOnClickListener {
                listener.onClick(gameItem)
            }
        }
    }

    private class GameDiff : DiffUtil.ItemCallback<GameCategorized>() {
        override fun areItemsTheSame(oldItem: GameCategorized, newItem: GameCategorized): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameCategorized, newItem: GameCategorized): Boolean {
            return oldItem == newItem
        }
    }
}