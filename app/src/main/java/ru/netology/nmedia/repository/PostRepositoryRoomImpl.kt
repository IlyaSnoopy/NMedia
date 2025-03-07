package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    override fun get(): LiveData<List<Post>>
    = dao.get().map { list -> list.map { it.toDto()}}

    override fun likeById(id: Long) = dao.likeById(id)

    override fun shareById(id: Long) = dao.shareById(id)

    override fun save(post: Post) = dao.save(PostEntity.fromDto(post))

    override fun removeById(id: Long) = dao.removeById(id)

}