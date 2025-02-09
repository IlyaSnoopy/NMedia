package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.security.PrivateKey

class PostAdapter(
    private val likeClickListener: OnLikeClicked,
    private val shareClickListener: OnShareClicked,
    private val removeClickListener: OnRemoveClicked
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding, likeClickListener, shareClickListener, removeClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}