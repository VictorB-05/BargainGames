package com.tfg.bargaingames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tfg.bargaingames.api.Constantes
import com.tfg.bargaingames.model.game.GameListAdapter
import com.tfg.bargaingames.api.GamesService
import com.tfg.bargaingames.databinding.FragmentHomeBinding
import com.tfg.bargaingames.model.search.GameSearchAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //private lateinit var adapter: GameListAdapter
    private lateinit var adapter: GameSearchAdapter
    private lateinit var service: GamesService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupRetrofit()
    }

    private fun setupAdapter() {
        adapter = GameSearchAdapter()
    }

    private  fun setupRecyclerView(){
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(GamesService::class.java)
    }

    private fun getGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.getStoreSearch("repo")
                //val games = response.specials?.items ?: emptyList() // Puedes elegir otra categoría aquí
                val games = response.games ?: emptyList()
                withContext(Dispatchers.Main) {
                    adapter.submitList(games)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener juegos", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getGames()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}