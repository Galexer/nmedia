package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl: PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология",
        content = "Привет!",
        published = "15.10.2022",
        likedByMe = false,
    )

    private var data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe)
        post = if (post.likedByMe) post.copy(postLikes = post.postLikes + 1)
            else post.copy(postLikes = post.postLikes - 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(share = post.share + 10)
        data.value = post
    }
}