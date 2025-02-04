package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.CountCalculator
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeClicked: OnLikeClicked,
    private val onShareClicked: OnShareClicked
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published

            like.setImageResource(
                if (post.likedByMe) {
                    R.drawable.ic_liked_24
                } else {
                    R.drawable.ic_like_24
                }
            )

            likeCount.text = CountCalculator.calculate(post.likes)

            shareCount.text = CountCalculator.calculate(post.shares)

            viewsCount.text = CountCalculator.calculate(post.views)

            like.setOnClickListener {
                onLikeClicked(post)
            }

            share.setOnClickListener {
                onShareClicked(post)
            }
        }
    }
}