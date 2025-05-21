package com.tfg.bargaingames

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tfg.bargaingames.api.Constantes.BASE_URL
import com.tfg.bargaingames.api.GamesService
import com.tfg.bargaingames.model.database.GameApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OfertasWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        val juegosDeseados = GameApplication.database.gameDao().getWishGame()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GamesService::class.java)

        val ids = juegosDeseados.joinToString(",") { it.id.toString() }

        val response = service.getAppDetails(appId = ids, filters = "price_overview")

        val juegosEnOferta = mutableListOf<String>()

        for (juego in juegosDeseados) {
            try {
                val data = response[juego.id.toString()]?.data

                if (data != null && data.price != null) {
                    juego.price = data.price
                    //GameApplication.database.gameDao().updateGame(juego)

                    if (data.price!!.discountPercent > 0) {
                        juegosEnOferta.add("${juego.name} - ${data.price!!.discountPercent}%")
                    }
                }
            } catch (e: Exception) {
                Log.e("OfertasWorker", "Error con juego ${juego.id}: ${e.message}")
            }
        }

        if (juegosEnOferta.isNotEmpty()) {
            mostrarNotificacion(context, juegosEnOferta.joinToString("\n"))
        }

        return Result.success()
    }

    private fun mostrarNotificacion(context: Context, texto: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "bargainGames")
            .setSmallIcon(R.drawable.favorite_24)
            .setContentTitle("¡Juegos en oferta!")
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }
}
