package ru.netology.nmedia.adapter

import android.icu.text.CompactDecimalFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.util.*

typealias OnLikeListener = (Post) -> Unit
typealias OnShareListener = (Post) -> Unit

class PostAdapter(
    private val likeOnClickListener: OnLikeListener,
    private val ShareOnClickListener: OnShareListener,
): ListAdapter<Post, PostViewHolder>(PostItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(likeOnClickListener, ShareOnClickListener, binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder (
    private val likeOnClickListener: OnLikeListener,
    private val ShareOnClickListener: OnShareListener,
    private var binding: CardPostBinding
        ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
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

            likesButton.setOnClickListener {
                likeOnClickListener(post)
            }
            shareButton.setOnClickListener {
                ShareOnClickListener(post)
            }
        }
    }
}

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

class PostItemCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

}

