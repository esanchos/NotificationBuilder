package com.earaujo.notificationbuilder

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.earaujo.notificationbuilder.NotificationCreation.NOTIFICATION_ID
import com.earaujo.notificationbuilder.NotificationCreation.WEB_WEB

class OpenChromeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            cancel(NOTIFICATION_ID)
        }
        val url = intent.getStringExtra(WEB_WEB)
        launchBrowser(url)
        finish()
    }

    private fun launchBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setPackage("com.android.chrome")
        }
        try {
            startActivity(intent)
        } catch (t: Throwable) {
            intent.setPackage(null)
            startActivity(intent)
        }
    }

}
