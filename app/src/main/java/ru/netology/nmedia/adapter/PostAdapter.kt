package ru.netology.nmedia.adapter

import android.icu.text.CompactDecimalFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.util.*

interface OnInteractionsListener {
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
}

class PostAdapter(
    private val onInteractionsListener: OnInteractionsListener
) : ListAdapter<Post, PostViewHolder>(PostItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(onInteractionsListener, binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val onInteractionsListener: OnInteractionsListener,
    private var binding: CardPostBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            published.text = post.published
            likesButton.isChecked = post.likedByMe
            likesButton.text = counter(post.postLikes)
            shareButton.text = counter(post.share)

            likesButton.setOnClickListener {
                onInteractionsListener.onLike(post)
            }
            shareButton.setOnClickListener {
                onInteractionsListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.optoins_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionsListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionsListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

private fun counter(num: Int) = if (Build.VERSION.SDK_INT >= 24) {
    CompactDecimalFormat.getInstance(Locale.US, CompactDecimalFormat.CompactStyle.SHORT).format(num)
} else {
    when (num) {
        in 0..999 -> num.toString()
        in 1_000..9_999 -> "${String.format("%.1f", num / 1000)}K"
        in 10_000..999_999 -> "${(num / 1000)}K"
        in 1_000_000..9_999_999 -> "${String.format("%.1f", num / 1_000_000)}M"
        else -> "too much"
    }
}

class PostItemCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

}

