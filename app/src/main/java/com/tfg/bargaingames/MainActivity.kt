package com.tfg.bargaingames

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tfg.bargaingames.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()
    }

    private fun setupBottomNav(){
        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()
        currentFragment = homeFragment

        with(supportFragmentManager) {
            beginTransaction()
                .add(R.id.nav_host, homeFragment)
                .hide(favouriteFragment).commit()

            beginTransaction()
                .add(R.id.nav_host,favouriteFragment)
                .commit()

            binding.navView.setOnItemSelectedListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.navigation_home -> {
                        beginTransaction().hide(currentFragment).show(homeFragment).commit()
                        currentFragment = homeFragment
                    }
                    R.id.navigation_favorite -> {
                        beginTransaction().hide(currentFragment).show(favouriteFragment).commit()
                        currentFragment = favouriteFragment
                    }
                }
                true
            }
        }

    }
}