package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl (private val dao: PostDao, context: Context): PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        dao.like(id)
        posts = posts.map {
            if (it.id == id) it.copy(
                likedByMe = !it.likedByMe, postLikes =
                if (!it.likedByMe) it.postLikes + 1 else it.postLikes - 1
            ) else it
        }
        data.value = posts
    }

    override fun share(id: Long) {
        dao.share(id)
        posts = posts.map { if (it.id == id) it.copy(share = it.share + 10) else it }
        data.value = posts
    }

    override fun remove(id: Long) {
        dao.remove(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id == id) it else saved
            }
        }
        data.value = posts
    }

    private val key = "content"
    private val pref = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    override fun saveContent(content: String) {
        pref.edit().apply{
            putString(key, content)
            apply()
        }
    }

    override fun getContent(): String{
        var content = ""
        pref.getString(key, null).let {
            content = it ?: ""
        }
        pref.edit().apply {
            clear()
            apply()
        }
        return content
    }

}