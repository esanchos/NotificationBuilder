package com.earaujo.notificationbuilder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.earaujo.notificationbuilder.NotificationCreation.TOPIC_SUBSCRIBE
import com.earaujo.notificationbuilder.NotificationCreation.createNotification
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFCM()

        btnNotification.setOnClickListener {
            createNotification(
                this, MainActivity::class.java, NotificationModel(
                    title = titleTextView.getString(),
                    content = contentTextView.getString(),
                    imageUrl = imageUrlTextView.getString(),
                    url = urlTextView.getString(),
                    actionText = actionTextTextView.getString()
                )
            )
        }
    }

    private fun TextView.getString() =
        if (this.text.isNotEmpty()) this.text.toString() else null


    private fun startFCM() {
        val token = FirebaseInstanceId.getInstance().token
        val msg = getString(R.string.msg_token_fmt, token)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_SUBSCRIBE)
        Log.d(TAG, msg)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
