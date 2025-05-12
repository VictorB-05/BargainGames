package com.tfg.bargaingames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tfg.bargaingames.api.Constantes
import com.tfg.bargaingames.model.game.GameListAdapter
import com.tfg.bargaingames.api.GamesService
import com.tfg.bargaingames.databinding.FragmentHomeBinding
import com.tfg.bargaingames.model.GameItem
import com.tfg.bargaingames.model.search.GameSearchAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: GameListAdapter
    private lateinit var searchAdapter: GameSearchAdapter
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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        buscarJuego(it)
                        mostrarResultados()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    mostrarCategorias()
                }
                return false
            }
        })
        setupAdapter()
        setupRecyclerView()
        setupRetrofit()
    }

    private fun setupAdapter() {
        listAdapter = GameListAdapter()
        listAdapter.setOnClickListener(this)

        searchAdapter = GameSearchAdapter()
        searchAdapter.setOnClickListener(this)
    }

    private  fun setupRecyclerView(){
        binding.recyclerViewCategorias.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = this@HomeFragment.listAdapter
        }
        binding.recyclerViewBusqueda.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = this@HomeFragment.searchAdapter
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
                val response = service.getFeaturedCategories()
                val games = response.specials?.items ?: emptyList()
                withContext(Dispatchers.Main) {
                    mostrarCategorias()
                    listAdapter.submitList(games)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener juegos", e)
                withContext(Dispatchers.Main) {
                    mostrarNoResultados()
                }
            }
        }
    }

    private fun buscarJuego(termino: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.getStoreSearch(termino, "spanish", "ES")
                val games = response.games ?: emptyList()
                withContext(Dispatchers.Main) {
                    if (games.isNotEmpty()) {
                        searchAdapter.submitList(games)
                        mostrarResultados()
                    } else {
                        mostrarNoResultados()
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error buscando juego", e)
                withContext(Dispatchers.Main) {
                    mostrarNoResultados()
                }
            }
        }
    }

    private fun mostrarNoResultados() {
        binding.recyclerViewCategorias.visibility = View.GONE
        binding.recyclerViewBusqueda.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
    }

    private fun mostrarResultados() {
        binding.recyclerViewCategorias.visibility = View.GONE
        binding.recyclerViewBusqueda.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
    }

    private fun mostrarCategorias() {
        binding.recyclerViewCategorias.visibility = View.VISIBLE
        binding.recyclerViewBusqueda.visibility = View.GONE
        binding.tvNoData.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        getGames()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(gameItem: GameItem) {
        Log.i("game", gameItem.toString())
        val fragment = GameDetailFragment.newInstance(gameItem.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host, fragment)
            .addToBackStack(null)
            .commit()
    }

}