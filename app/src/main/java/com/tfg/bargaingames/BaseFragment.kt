package com.tfg.bargaingames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tfg.bargaingames.api.Constantes
import com.tfg.bargaingames.api.GamesService
import com.tfg.bargaingames.databinding.FragmentHomeBinding
import com.tfg.bargaingames.model.GameItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseFragment : Fragment(), OnClickListener{
    protected lateinit var service: GamesService

    protected fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(GamesService::class.java)
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