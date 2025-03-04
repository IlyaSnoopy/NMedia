package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl
import ru.netology.nmedia.repository.PostRepositoryInMemory

val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFilesImpl(application)

    val data: LiveData<List<Post>> = repository.get()

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun editContent(post: Post) {
        edited.value = post
    }

    fun editClear() {
        edited.value = empty
    }

    fun saveContent() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}