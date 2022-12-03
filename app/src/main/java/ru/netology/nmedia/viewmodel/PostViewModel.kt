package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInFilesImpl
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private var empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
)

class PostViewModel (application: Application) : AndroidViewModel(application) {
    //private val repository: PostRepository = PostRepositoryInFilesImpl(application)
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao,
        application
    )
    val data = repository.get()
    val edited = MutableLiveData(empty)

    fun like(id: Long) = repository.like(id)
    fun share(id: Long) = repository.share(id)
    fun remove(id: Long) = repository.remove(id)
    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            val url = if (content.contains("https://www.youtube.com/")) {
                 "https://www.youtube.com/" + youtubeLinkTaker(content)
            } else ""
            edited.value?.let {
                repository.save(it.copy(content = text, video = url))
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun youtubeLinkTaker(text: String): String {
        val halfUrl = text.substringAfter("https://www.youtube.com/")
        if (halfUrl.contains(" ")) {
            return halfUrl.substringBefore(' ')
        }
        return halfUrl
    }

    fun clear() {
        edited.value = empty
    }

    fun chasContent(text: String) {
        repository.saveContent(text)
    }
    fun getContent(): String {
        return repository.getContent()
    }
}