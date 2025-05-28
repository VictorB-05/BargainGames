package com.tfg.bargaingames

import androidx.fragment.app.Fragment
import com.tfg.bargaingames.api.Constantes
import com.tfg.bargaingames.api.GamesService
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
        val fragment = GameDetailFragment.newInstance(gameItem.id)
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        fragmentManager.fragments.forEach {
            if (it.isVisible) transaction.hide(it)
        }

        transaction
            .add(R.id.nav_host, fragment)
            .addToBackStack(null)
            .commit()
    }

}