package ru.netology.nmedia.dto

data class Post (
    val id: Long = 0,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val postLikes: Int = 0,
    val share : Int = 990,
)