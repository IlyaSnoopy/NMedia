package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val video: String = ""
) {
    fun toDto() = Post(
        id,
        author,
        content,
        published,
        likedByMe,
        likes,
        shares,
        views,
        video)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likedByMe,
            post.likes,
            post.shares,
            post.views,
            post.video
        )
    }
}