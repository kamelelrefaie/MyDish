package com.example.mydish.model.notitifcation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mydish.R
import com.example.mydish.utils.Constants
import com.example.mydish.view.activities.MainActivity


class NotifyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        sendNotification()
        return success()
    }

    private fun sendNotification() {
        val notificationId = 0

        //create notification manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_id,
                    Constants.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    setSound(ringtoneManager, audioAttributes)
                }

            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_share)

        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, FLAG_MUTABLE)

        //create notification
        val notification =
            NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_id)
                .setContentTitle(titleNotification)
                .setContentText(subtitleNotification)
                .setSmallIcon(R.drawable.ic_add)
                .setLargeIcon(bitmap)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setStyle(bigPicStyle)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

        //notification manager for notification
        val notificationManagerBuilder = NotificationManagerCompat.from(applicationContext)
        notificationManagerBuilder.notify(notificationId, notification)
    }

    private fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}