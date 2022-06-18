package com.example.shopingofmine.ui.notificationworkmanager

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shopingofmine.R
import com.example.shopingofmine.ui.NOTIFICATION_CHANNEL_ID
import com.example.shopingofmine.ui.mainactivity.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.random.Random

@HiltWorker
class NotificationWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {

        return try {
            val count = inputData.getInt("count", 0)
            NotificationManagerCompat.from(applicationContext).notify(Random.nextInt(), createNotification(count))
             Result.success()
        } catch (e: Throwable) {
             Result.failure()
        }
    }

    private fun createNotification(count: Int): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_black_cart)
            .setContentTitle("خریدت یادت نره!")
            .setContentText("شما $count کالا در سبد خرید خود دارید.")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()
    }
}