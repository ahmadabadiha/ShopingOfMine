package com.example.shopingofmine.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.shopingofmine.R
import com.example.shopingofmine.ui.mainactivity.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.IllegalStateException
import kotlin.random.Random

@HiltWorker
class NotificationWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {
        val count = inputData.getInt("count", 0)
        try {
            startForegroundService(count)
        } catch (e: IllegalStateException) {
            return Result.retry()
        }
        return Result.success()
    }

    private suspend fun startForegroundService(count: Int) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                createNotification(count)
            )
        )
    }

    private fun createNotification(count: Int): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(applicationContext, "shopping channel")
            .setSmallIcon(R.drawable.ic_black_cart)
            .setContentTitle("خریدت یادت نره!")
            .setContentText("شما $count کالا در سبد خرید خود دارید. همین حالا سفارش خود را کامل کنید.")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()
    }
}