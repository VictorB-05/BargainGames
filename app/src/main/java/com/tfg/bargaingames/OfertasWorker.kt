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
        var ids = ""

        //Juegos deseados de la base de datos
        val juegosDeseados = GameApplication.database.gameDao().getWishGame()
        for(juego in juegosDeseados){
            if(juego.price != null){
                ids += juego.id.toString()+","
            }
        }
        ids = ids.removeSuffix(",")

        //Respuesta de la api de Steam sobre nuestros juegos favoritos
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GamesService::class.java)
        val response = service.getAppDetails(appId = ids, filters = "price_overview")

        // Array donde se meten las altertas de rebajas
        val juegosEnOferta = mutableListOf<String>()
        for (juego in juegosDeseados) {
            val data = response[juego.id]?.data

            if (data?.price != null && data.price != juego.price) {
                if (data.price!!.discountPercent > juego.price!!.discountPercent) {
                    juegosEnOferta.add("${juego.name} - ${data.price!!.discountPercent}% - ${data.price!!.finalFormatted}")
                }
                juego.price = data.price
                GameApplication.database.gameDao().update(juego)
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
            .setSmallIcon(R.drawable.icon_games)
            .setContentTitle("¡Juegos en oferta!")
            .setContentText("Juegos de tu lista de deseados están en oferta")
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }
}
