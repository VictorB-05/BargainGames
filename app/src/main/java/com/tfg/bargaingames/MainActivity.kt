package com.tfg.bargaingames

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.tfg.bargaingames.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        notficationChanel()
        workerDiario()
        probarWorker()
    }

    private fun setupBottomNav() {
        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()

        with(supportFragmentManager){
            beginTransaction()
                .add(R.id.nav_host, homeFragment, "home")
                .add(R.id.nav_host, favouriteFragment, "favourite")
                .hide(favouriteFragment)
                .commit()

            binding.navView.setOnItemSelectedListener { menuItem ->

                val fragmentToShow : Fragment = if(menuItem.itemId == R.id.navigation_home){
                    homeFragment
                }else{
                    favouriteFragment
                }

                beginTransaction().apply {
                    fragments.forEach { hide(it) }
                    show(fragmentToShow)
                    commit()
                }
                true
            }
        }
    }

    private fun probarWorker() {
        val testRequest = OneTimeWorkRequestBuilder<OfertasWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(testRequest)
    }

    private fun workerDiario() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dailyRequest = PeriodicWorkRequestBuilder<OfertasWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calcularHora(), TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ofertas_diarias",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }

    private fun calcularHora(): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return target.timeInMillis - now.timeInMillis
    }

    private fun notficationChanel() {
        val channel = NotificationChannel(
            "bargainGames",
            "Ofertas de Steam",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de ofertas de juegos deseados"
        }

        val manager = this.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}