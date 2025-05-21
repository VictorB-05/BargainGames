package com.tfg.bargaingames

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.tfg.bargaingames.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    companion object{
        const val MY_CHANEL_ID = "bargainGames"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        crearCanalNotificaciones()
        programarWorkerDiario()
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

    fun programarWorkerDiario() {
        val dailyRequest = PeriodicWorkRequestBuilder<OfertasWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calcularHora(), TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ofertas_diarias",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyRequest
        )
    }

    fun calcularHora(): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 35)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1) // mañana
            }
        }
        return target.timeInMillis - now.timeInMillis
    }

    fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                "bargainGames",
                "Ofertas de Steam",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de ofertas de juegos deseados"
            }

            val manager = this.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
}