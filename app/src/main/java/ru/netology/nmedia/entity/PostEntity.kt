package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val author: String = "",
        val content: String = "",
        val published: String = "",
        val likedByMe: Boolean = false,
        val postLikes: Int = 0,
        val share : Int = 0,
        val video: String = ""
        ) {
        fun toDto() = Post(id = id, author = author, content = content, published = published,
        likedByMe = likedByMe, postLikes = postLikes, share = share, video = video)

        companion object {
                fun  fromDto(post: Post) = PostEntity(id = post.id, author = post.author,
                        content = post.content, published = post.published,
                        likedByMe = post.likedByMe, postLikes = post.postLikes, share = post.share,
                        video = post.video)
        }
}