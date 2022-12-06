package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query( """
                UPDATE PostEntity SET
               postLikes = postLikes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id;
            """)
    fun like(id: Long)

    @Query("""
                UPDATE PostEntity SET
               share = share + 10
           WHERE id = :id;
            """)
    fun share(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun remove(id: Long)

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id ")
    fun updateContentById(id: Long, content: String)

    fun save(post: PostEntity) = if (post.id == 0L) insert(post) else
        updateContentById(post.id, post.content)
}