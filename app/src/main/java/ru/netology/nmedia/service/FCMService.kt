package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.NotificationParams
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val channelId = "100"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println(Gson().toJson(message))
        message.data["action"]?.let {
            if (it != "LIKE" || it != "POST") {
                onIncorrectNotify()
            }
            when (Action.valueOf(it)) {
                Action.LIKE -> handleLike(
                    Gson().fromJson(
                        message.data["content"],
                        Like::class.java
                    )
                )
                Action.POST -> handlePost(
                    Gson().fromJson(
                        message.data["content"],
                        PostNotify::class.java
                    )
                )

            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    private fun handleLike(like: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(getString(R.string.notification_user_liked, like.userName, like.postAuthor))
            .build()
        NotificationManagerCompat.from(this).notify(Random.nextInt(100000), notification)
    }

    private fun handlePost(postNotify: PostNotify) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(getString(R.string.notification_new_post, postNotify.postAuthor))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(postNotify.postContent)
            )
            .build()
        NotificationManagerCompat.from(this).notify(Random.nextInt(100000), notification)
    }

    private fun onIncorrectNotify() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(getString(R.string.notification_incorrect))
            .build()
        NotificationManagerCompat.from(this).notify(Random.nextInt(100000), notification)
    }
}

enum class Action {
    LIKE,
    POST
}

data class Like(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String,
)

data class PostNotify(
    val authorId: Int,
    val postAuthor: String,
    val postContent: String,
    val postId: Int,
)