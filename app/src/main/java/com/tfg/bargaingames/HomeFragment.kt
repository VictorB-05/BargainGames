package com.tfg.bargaingames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tfg.bargaingames.databinding.FragmentHomeBinding
import com.tfg.bargaingames.model.game.GameCategorized
import com.tfg.bargaingames.model.game.GameListAdapter
import com.tfg.bargaingames.model.search.GameSearchAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: GameListAdapter
    private lateinit var searchAdapter: GameSearchAdapter
    private var search: String = ""

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
                        search = it
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

    private fun getGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val list = ArrayList<GameCategorized>()
                val response = service.getFeaturedCategories()
                val games = response.specials?.items ?: emptyList()
                val topSellers = response.topSellers?.items ?: emptyList()
                val newReleases = response.newReleases?.items ?: emptyList()
                list.addAll(games)
                list.addAll(topSellers)
                list.addAll(newReleases)
                withContext(Dispatchers.Main) {
                    if (games.isNotEmpty()) {
                        mostrarCategorias()
                        listAdapter.submitList(list)
                    }else{
                        mostrarNoResultados()
                    }
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
        if(search.isNotEmpty()){
            buscarJuego(search)
            mostrarResultados()
        }else{
            getGames()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}