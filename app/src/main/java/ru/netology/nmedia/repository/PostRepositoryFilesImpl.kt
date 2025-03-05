package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {

    companion object {
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
        private const val FILENAME = "posts.json"
    }

    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILENAME)
        if (file.exists()) {
            context.openFileInput(FILENAME).bufferedReader().use {
                posts = gson.fromJson(it, type)
                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
                data.value = posts
            }
        }
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    published = "Now",
                    author = "Me"
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }

        }
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) {
                        post.likes - 1
                    } else {
                        post.likes + 1
                    }
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    shares = post.shares + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    private fun sync() {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}