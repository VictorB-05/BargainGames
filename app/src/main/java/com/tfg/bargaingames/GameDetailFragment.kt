package com.tfg.bargaingames


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var gameDB: GameData? = null

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
        seeDatabase()
        setupRetrofit()
        obtenerDetallesJuego()
        binding.cbFavorite.setOnClickListener {
            addFavorite(view)
        }
        binding.cbAdd.setOnClickListener{
            addWish(view)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
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

                            if(game.price!=null){
                                val precio = game.price!!.finalFormatted
                                Log.i("game",precio)
                                price.text = precio
                            }else{
                                price.text = "Gratis"
                            }

                            if(gameDB!=null && gameDB!!.favorito){
                                cbFavorite.setImageResource(R.drawable.favorite_24)
                            }

                            if(gameDB!=null && gameDB!!.deseado){
                                cbAdd.setImageResource(R.drawable.add_circle_24)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GameDetail", "Error al obtener detalles", e)
            }
        }
    }

    private fun seeDatabase(){
        lifecycleScope.launch(Dispatchers.IO) {
            gameDB = GameApplication.database.gameDao().getGame(appId)
        }
    }

    private fun addFavorite(view: View){
        lifecycleScope.launch(Dispatchers.IO) {
            if(gameDB != null) {
                if (gameDB!!.favorito){
                    GameApplication.database.gameDao().updateFavorito(game.id, false)
                    gameDB!!.favorito = false
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego quitado de favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbFavorite.setImageResource(R.drawable.favorite_no_24)
                        }
                    }
                }else {
                    GameApplication.database.gameDao().updateFavorito(game.id, true)
                    gameDB!!.favorito = true
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbFavorite.setImageResource(R.drawable.favorite_24)
                        }
                    }
                }
            }else{
                game.favorito = true
                val result = GameApplication.database.gameDao().addGame(game)
                gameDB = game
                if (result != -1L) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a favoritos", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbFavorite.setImageResource(R.drawable.favorite_24)
                        }
                    }
                }
            }
        }
    }

    private fun addWish(view: View){
        lifecycleScope.launch(Dispatchers.IO) {
            if(gameDB != null) {
                if (gameDB!!.deseado){
                    GameApplication.database.gameDao().updateDeseado(game.id, false)
                    gameDB!!.deseado = false
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego quitado de deseados", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbAdd.setImageResource(R.drawable._add_circle_no_24)
                        }
                    }
                }else {
                    GameApplication.database.gameDao().updateDeseado(game.id, true)
                    gameDB!!.deseado = true
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a deseados", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbAdd.setImageResource(R.drawable.add_circle_24)
                        }
                    }
                }
            }else{
                game.deseado = true
                val result = GameApplication.database.gameDao().addGame(game)
                gameDB = game
                if (result != -1L) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Juego añadido a deseados", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.apply {
                            cbAdd.setImageResource(R.drawable.add_circle_24)
                        }
                    }
                }
            }
        }
    }
}
