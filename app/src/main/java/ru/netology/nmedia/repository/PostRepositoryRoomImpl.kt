package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl (private val dao: PostDao, context: Context): PostRepository {

    override fun get() = dao.getAll().map { list -> list.map { it.toDto() } }

    override fun like(id: Long) = dao.like(id)

    override fun share(id: Long) = dao.share(id)

    override fun remove(id: Long) = dao.remove(id)

    override fun save(post: Post) = dao.save(PostEntity.fromDto(post))

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