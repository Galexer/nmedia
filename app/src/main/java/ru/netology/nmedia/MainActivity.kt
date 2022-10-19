package ru.netology.nmedia

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.util.*

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val postLikes: Int = 0,
    val share : Int = 990,
        )

fun counter (num: Int) = if(Build.VERSION.SDK_INT >= 24) {
         CompactDecimalFormat.getInstance(Locale.US, CompactDecimalFormat.CompactStyle.SHORT).format(num)
    } else {
         when(num) {
            in 0..999 -> num.toString()
            in 1_000..9_999 -> "${String.format("%.1f", num/1000)}K"
            in 10_000..999_999 -> "${(num / 1000)}K"
            in 1_000_000..9_999_999 -> "${String.format("%.1f", num/1_000_000)}M"
            else -> "too much"
        }
    }

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1,
            author = "Нетология",
            content = "Привет!",
            published = "15.10.2022",
            likedByMe = false,
        )

        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            published.text = post.published
            if (!post.likedByMe) {
                likesButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            likes.text = counter(post.postLikes)
            shares.text = counter(post.share)

            likesButton.setOnClickListener {
                post = post.copy(likedByMe = !post.likedByMe)
                likesButton.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
                post = if (post.likedByMe) post.copy(postLikes = post.postLikes + 1)
                else post.copy(postLikes = post.postLikes - 1)
                val formatCountLike = counter(post.postLikes)
                likes.text = formatCountLike
            }

            shareButton.setOnClickListener {
                post = post.copy(share = post.share + 10)
                val formatCountShare = counter(post.share)
                shares.text = formatCountShare
            }
        }
    }
}