package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
    fun remove(id: Long)
    fun save(post: Post)
    fun saveContent (content: String)
    fun getContent(): String
}