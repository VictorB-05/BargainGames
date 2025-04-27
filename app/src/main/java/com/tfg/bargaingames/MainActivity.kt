package com.tfg.bargaingames

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tfg.bargaingames.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host,homeFragment)
            .commit()
    }
}