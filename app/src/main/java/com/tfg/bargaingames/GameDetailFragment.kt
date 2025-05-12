package com.tfg.bargaingames


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.tfg.bargaingames.api.Constantes
import com.tfg.bargaingames.api.GamesService
import com.tfg.bargaingames.databinding.GameDetailBinding
import com.tfg.bargaingames.model.database.GameApplication
import com.tfg.bargaingames.model.detail.GameData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDetailFragment : Fragment() {
    private var _binding: GameDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var service: GamesService
    private var appId: Int = 0
    private lateinit var game: GameData

    companion object {
        private const val ARG_APP_ID = "app_id"

        fun newInstance(appId: Int): GameDetailFragment {
            val fragment = GameDetailFragment()
            val args = Bundle()
            args.putInt(ARG_APP_ID, appId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            appId = it.getInt(ARG_APP_ID)
        }
        _binding = GameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRetrofit() {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Puedes cambiar a BASIC o HEADERS
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(GamesService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        obtenerDetallesJuego()
        binding.cbFavorite.setOnClickListener {
            addFavorite(view)
        }
    }

    private fun obtenerDetallesJuego() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.getAppDetails(appId)
                val appDetails = response[appId.toString()]
                if (appDetails?.success == true && appDetails.data != null) {
                    game = appDetails.data
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            name.text = game.name
                            description.text = HtmlCompat.fromHtml(game.description, HtmlCompat.FROM_HTML_MODE_LEGACY)


                            Log.i("game",game.image)

                            context?.let {
                                Glide.with(it)
                                    .load(game.image)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(imageGame)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GameDetail", "Error al obtener detalles", e)
            }
        }
    }

    private fun addFavorite(view: View){
        lifecycleScope.launch(Dispatchers.IO) {
            val gameDb = GameApplication.database.gameDao().getGame(game.id)
            if(gameDb != null) {
                if (gameDb.favorito){
                    GameApplication.database.gameDao().updateFavorito(gameDb.id, false)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego quitado de favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }else {
                    val result = GameApplication.database.gameDao().updateFavorito(gameDb.id, true)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }else{
                val result = GameApplication.database.gameDao().addGame(game)
                if (result != -1L) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
