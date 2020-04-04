package com.earaujo.notificationbuilder

data class NotificationModel(
    val title: String? = null,
    val content: String? = null,
    val imageUrl: String? = null,
    val url: String? = null,
    val actionText: String? = null
)