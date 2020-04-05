package com.earaujo.notificationbuilder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

object NotificationCreation {

    fun <T> createNotification(context: Context, cls: Class<T>, model: NotificationModel) {
        createNotificationChannel(context)

        val mainPIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, cls).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_ONE_SHOT
        )

        NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(model.title)
            setContentText(model.content)
            setAutoCancel(true)
            setContentIntent(mainPIntent)
            priority = NotificationCompat.PRIORITY_DEFAULT
            color = ContextCompat.getColor(context, R.color.colorPrimary)

            getActionPendingIntent(context, model.url)?.let {
                addAction(R.drawable.ic_like, model.actionText, it)
            }

            model.imageUrl?.let {
                loadBitmap(context, it) { bitmap ->
                    setLargeIcon(bitmap)
                    setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                    //setStyle(NotificationCompat.BigTextStyle().bigText("").setBigContentTitle(""))
                    showNotification(context, build())
                }
            } ?: run {
                showNotification(context, build())
            }
        }
    }

    private fun showNotification(context: Context, notification: Notification) {
        NotificationManagerCompat.from(context).apply {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun loadBitmap(context: Context, url: String, callback: (Bitmap) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback(bitmap)
                }
            })
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = CHANNEL_DESCRIPTION
                })
            }
        }
    }

    private fun getActionPendingIntent(context: Context, url: String?): PendingIntent? {
        return url?.let {
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, OpenChromeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra(WEB_WEB, url)
                },
                PendingIntent.FLAG_ONE_SHOT
            )
        }
    }

    const val TOPIC_SUBSCRIBE = "all"
    const val WEB_WEB = "url"
    private const val CHANNEL_ID = "channel_i01"
    const val NOTIFICATION_ID = 1
    private const val CHANNEL_NAME = "My Notification"
    private const val CHANNEL_DESCRIPTION = "My notification description"

}
