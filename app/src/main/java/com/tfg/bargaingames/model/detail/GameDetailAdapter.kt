package com.tfg.bargaingames.model.detail

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

class GameDetailAdapter: ListAdapter<GameData, RecyclerView.ViewHolder>(GameStoreDiff()) {

    private lateinit var context: Context
    private lateinit var listener: OnClickListener
    val deseado: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val GameData = getItem(position)
        (holder as ViewHolder).run {
            setListener(GameData)
            with(binding) {
                Nombre.text = GameData.name
                Precio.text = GameData.price?.finalFormatted ?: "Sin precio"

                if(GameData.free){
                    Precio.text = "Gratis"
                }

                Glide.with(context)
                    .load(GameData.capsuleImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageGame)
            }
            CoroutineScope(Dispatchers.IO).launch {
                val gameDb = GameApplication.database.gameDao().getGame(GameData.id)
                withContext(Dispatchers.Main) {
                    with(binding) {
                        if (gameDb?.favorito == true) {
                            Log.i("game", GameData.name)
                            cbFavorite.setImageResource(R.drawable.favorite_24)
                        } else {
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

    private fun seeDatabase (id : Int): GameData? {
        var gameDb: GameData? = null
        CoroutineScope(Dispatchers.IO).launch {
            gameDb = GameApplication.database.gameDao().getGame(id)
        }
        return gameDb
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGameBinding.bind(view)

        fun setListener(gameItem: GameItem) {
            binding.root.setOnClickListener {
                listener.onClick(gameItem)
            }
        }
    }

    private class GameStoreDiff : DiffUtil.ItemCallback<GameData>() {
        override fun areItemsTheSame(oldItem: GameData, newItem: GameData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameData, newItem: GameData): Boolean {
            return oldItem == newItem
        }
    }

}
