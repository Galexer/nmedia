package ru.netology.nmedia

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.util.*
import androidx.activity.viewModels
import ru.netology.nmedia.viewmodel.PostViewModel


private fun counter (num: Int) = if(Build.VERSION.SDK_INT >= 24) {
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

    private val viewModel: PostViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                published.text = post.published
                if (!post.likedByMe)
                    likesButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                else likesButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                likes.text = counter(post.postLikes)
                shares.text = counter(post.share)
            }
        }

        binding.likesButton.setOnClickListener {
            viewModel.like()
        }

        binding.shareButton.setOnClickListener {
            viewModel.share()
        }
    }
}