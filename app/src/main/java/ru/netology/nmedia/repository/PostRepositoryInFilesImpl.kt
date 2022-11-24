package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryInFilesImpl(val context: Context) : PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private var data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            try {
                context.openFileInput(fileName).bufferedReader().use {
                    posts = gson.fromJson(it, type)
                    nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
                }
            } catch (rte: RuntimeException) {
                posts = listOf(
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
                        video = "https://www.youtube.com/watch?v=RNprUxOGUUw"
                    )
                )
            }
        } else {
            posts = listOf(
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
                    video = "https://www.youtube.com/watch?v=RNprUxOGUUw"
                )
            )
        }
        data.value = posts
    }

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

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}