package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.empty

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    val editPostLauncher = registerForActivityResult(EditPostContract) { result ->
        if (result == null) {
            viewModel.editContent(empty)
        } else {
            viewModel.changeContent(result)
            viewModel.saveContent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyInset(binding.root)

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                viewModel.shareById(post.id)
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.editContent(post)
                editPostLauncher.launch(post.content)
            }

            override fun onVideoClick(videUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videUrl))
                startActivity(intent)
            }
        }
        )

        binding.main.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.main.smoothScrollToPosition(0)
                }
            }
        }

        val newPostLauncher = registerForActivityResult(NewPostContract) { content ->
            content ?: return@registerForActivityResult
            viewModel.changeContent(content)
            viewModel.saveContent()
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
        }

        binding.add.setOnClickListener {
            newPostLauncher.launch(Unit)
        }
    }

    private fun applyInset(main: View) {
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isImeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                if (isImeVisible) imeInsets.bottom else systemBars.bottom
            )
            insets
        }
    }
}