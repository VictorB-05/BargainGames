package com.tfg.bargaingames

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tfg.bargaingames.databinding.ActivityMainBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GameListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupRecyclerView()
    }

    private fun setupAdapter() {
        adapter = GameListAdapter()
    }

    private  fun setupRecyclerView(){
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = this@MainActivity.adapter

        }
    }

    private fun getGames() {
        val games = getLocalGames()
        adapter.submitList(games)
    }

    private fun getLocalGames() = listOf(
        Game(
            id = 3241660,
            type = "0",
            name = "R.E.P.O.",
            discounted = false,
            discountedPercent = 0,
            originalPrice = 999,
            finalPrice = 999,
            currency = "USD",
            largeImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3241660/2cff5912c1add2e009eb1c1c630a47ac06cb81a1/capsule_616x353.jpg?t=1743517226",
            smallImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3241660/28c1bb423e9af8646047e9e881c3a0ac121647f6/capsule_184x69.jpg?t=1743517226",
            discountExpiration = LocalDate.MAX
        ),
        Game(
            id = 1086940,
            type = "0",
            name = "Baldur's Gate 3",
            discounted = true,
            discountedPercent = 20,
            originalPrice = 5999,
            finalPrice = 4799,
            currency = "USD",
            largeImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1086940/capsule_616x353.jpg?t=1740386911",
            smallImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1086940/capsule_184x69.jpg?t=1740386911",
            discountExpiration = Instant.ofEpochSecond(1745254800).atZone(ZoneId.systemDefault()).toLocalDate()
        ),
        Game(
            id = 2456740,
            type = "0",
            name = "inZOI",
            discounted = false,
            discountedPercent = 0,
            originalPrice = 3999,
            finalPrice = 3999,
            currency = "USD",
            largeImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2456740/169fdacc61e59aa8d0272b3a4f1e93c8dfe8d18a/capsule_616x353.jpg?t=1744272005",
            smallImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2456740/db7e09bed342057b3ae057d5a2fcfb94a37f0762/capsule_184x69.jpg?t=1744272005",
            discountExpiration = LocalDate.MAX
        ),
        Game(
            id = 2531310,
            type = "0",
            name = "The Last of Us™ Part II Remastered",
            discounted = false,
            discountedPercent = 0,
            originalPrice = 4999,
            finalPrice = 4999,
            currency = "USD",
            largeImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2531310/94b5d8b3165a6fe592e406054b08a2dd24e2f848/capsule_616x353.jpg?t=1743700473",
            smallImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2531310/29904ca69ae022479d64207cc40ce549d0a68acb/capsule_184x69.jpg?t=1743700473",
            discountExpiration = LocalDate.MAX
        ),
        Game(
            id = 2246340,
            type = "0",
            name = "Monster Hunter Wilds",
            discounted = false,
            discountedPercent = 0,
            originalPrice = 6999,
            finalPrice = 6999,
            currency = "USD",
            largeImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2246340/a1df989a3b439e15171dc7144c1ce13c32abcae6/capsule_616x353.jpg?t=1743743917",
            smallImage = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2246340/970508d58e4f11f6f9ff3e2921b7eec558a50af8/capsule_184x69.jpg?t=1743743917",
            discountExpiration = LocalDate.MAX
        )
    )

    override fun onResume() {
        super.onResume()
        getGames()
    }


}