package com.earaujo.notificationbuilder

import android.util.Log
import com.earaujo.notificationbuilder.NotificationCreation.createNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            handleNotification(remoteMessage)
        }
    }

    private fun handleNotification(remoteMessage: RemoteMessage) {
        val imageUrl = remoteMessage.data["image_url"]
        val url = remoteMessage.data["url"]
        val title = remoteMessage.data["title"] ?: ""
        val content = remoteMessage.data["content"] ?: ""
        val actionText = remoteMessage.data["action_text"] ?: "Abrir"
        createNotification(
            this,
            MainActivity::class.java,
            NotificationModel(
                title = title,
                imageUrl = imageUrl,
                url = url,
                content = content,
                actionText = actionText
            )
        )
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}