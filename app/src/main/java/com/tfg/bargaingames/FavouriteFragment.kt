package com.tfg.bargaingames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tfg.bargaingames.databinding.FragmentFavoriteBinding
import com.tfg.bargaingames.model.database.GameApplication
import com.tfg.bargaingames.model.detail.GameDetailAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : BaseFragment() {
    private lateinit var detailAdapter: GameDetailAdapter
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var favorite: Boolean = false
    private var wish: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupRetrofit()

        binding.buttonFavorite.setOnClickListener{
            if(favorite){
                getGames()
                favorite = false
            }else{
                getFavoritesGames()
                favorite = true
                wish = false
            }

        }

        binding.buttonDeseados.setOnClickListener{
            if(wish){
                getGames()
                wish = false
            }else{
                getWishGames()
                wish = true
                favorite = false
            }
        }
    }

    private fun setupAdapter() {
        detailAdapter = GameDetailAdapter()
        detailAdapter.setOnClickListener(this)
    }

    private  fun setupRecyclerView(){
        binding.recyclerViewFavorites.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = this@FavouriteFragment.detailAdapter
        }
    }

    private fun getGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val games = GameApplication.database.gameDao().getAllGame()
                withContext(Dispatchers.Main) {
                    mostrarResultados()
                    detailAdapter.submitList(games)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener juegos", e)
                withContext(Dispatchers.Main) {
                    mostrarNoResultados()
                }
            }
        }
    }

    private fun getFavoritesGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val games = GameApplication.database.gameDao().getFavoriteGame()
                withContext(Dispatchers.Main) {
                    mostrarResultados()
                    detailAdapter.submitList(games)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener juegos", e)
                withContext(Dispatchers.Main) {
                    mostrarNoResultados()
                }
            }
        }
    }

    private fun getWishGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val games = GameApplication.database.gameDao().getWishGame()
                withContext(Dispatchers.Main) {
                    mostrarResultados()
                    detailAdapter.submitList(games)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener juegos", e)
                withContext(Dispatchers.Main) {
                    mostrarNoResultados()
                }
            }
        }
    }

    private fun mostrarNoResultados() {
        binding.recyclerViewFavorites.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
    }

    private fun mostrarResultados() {
        binding.recyclerViewFavorites.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if(favorite){
            getFavoritesGames()
        }else if (wish){
            getWishGames()
        }else{
            getGames()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}