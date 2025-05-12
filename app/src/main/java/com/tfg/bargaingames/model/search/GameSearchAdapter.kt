package com.tfg.bargaingames.model.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.tfg.bargaingames.OnClickListener
import com.tfg.bargaingames.R
import com.tfg.bargaingames.databinding.ItemGameBinding
import com.tfg.bargaingames.model.GameItem
import com.tfg.bargaingames.model.database.GameApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameSearchAdapter: ListAdapter<GameStore, RecyclerView.ViewHolder>(GameStoreDiff()) {

    private lateinit var context: Context
    private lateinit var listener: OnClickListener
    val deseado: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gameStore = getItem(position)
        (holder as ViewHolder).run {
            CoroutineScope(Dispatchers.IO).launch {
                val gameDb = GameApplication.database.gameDao().getGame(gameStore.id)
                withContext(Dispatchers.Main) {
                    setListener(gameStore)
                    with(binding) {
                        Nombre.text = gameStore.name
                        Precio.text = gameStore.price?.let {
                            (it.final.toFloat() / 100).toString() + " " + it.currency
                        } ?: "Sin precio"

                        Glide.with(context)
                            .load(gameStore.tinyImage)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(imageGame)

                        if (gameDb?.favorito == true) {
                            Log.i("game",gameStore.name)
                            cbFavorite.setImageResource(R.drawable.favorite_24)
                        }else {
                            cbFavorite.setImageResource(R.drawable.favorite_no_24)
                        }
                        if (gameDb?.deseado == true) {
                            cbDeseado.setImageResource(R.drawable.add_circle_24)
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

    private class GameStoreDiff : DiffUtil.ItemCallback<GameStore>() {
        override fun areItemsTheSame(oldItem: GameStore, newItem: GameStore): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameStore, newItem: GameStore): Boolean {
            return oldItem == newItem
        }
    }
}
