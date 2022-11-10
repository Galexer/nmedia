package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = -1,
            author = "Нетология",
            content = "Привет!",
            published = "15.10.2022",
            likedByMe = false,
        ),
        Post(
            id = -2,
            author = "Нетология",
            content = "Привет! Пост 2",
            published = "15.10.2022",
            likedByMe = false,
        )
    )

    private var data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map {
            if (it.id == id) it.copy(
                likedByMe = !it.likedByMe, postLikes =
                if (!it.likedByMe) it.postLikes + 1 else it.postLikes - 1
            ) else it
        }
        data.value = posts
    }

    override fun share(id: Long) {
        posts = posts.map { if (it.id == id) it.copy(share = it.share + 10) else it }
        //post = post.copy(share = post.share + 10)
        data.value = posts
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "нетолония", published = "now")) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
    }
}