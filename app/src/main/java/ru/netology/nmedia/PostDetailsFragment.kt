package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class PostDetailsFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        val postId = arguments?.getLong("postId") ?: 0L

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId }
            if (post != null) {
                updateUI(binding, post)
            }
        }
        return binding.root
    }

    private fun updateUI(binding: FragmentPostDetailsBinding, post: Post) {
        binding.author.text = post.author
        binding.content.text = post.content
        binding.published.text = post.published
        binding.like.isChecked = post.likedByMe
        binding.like.text = CountCalculator.calculate(post.likes)
        binding.share.text = CountCalculator.calculate(post.shares)
        binding.views.text = CountCalculator.calculate(post.views)

        binding.like.setOnClickListener {
            viewModel.likeById(post.id)
        }

        binding.share.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            viewModel.shareById(post.id)
            val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(chooser)
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            viewModel.removeById(post.id)
                            findNavController().navigateUp()
                            true
                        }

                        R.id.edit -> {
                            viewModel.editContent(post)
                            val bundle = Bundle().apply {
                                putString("editedText", post.content)
                            }
                            findNavController().navigate(
                                R.id.action_postDetailsFragment_to_editPostFragment,
                                bundle
                            )
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        post.video?.let { videoUrl ->
            binding.videoCard.visibility = View.VISIBLE
            binding.videoCard.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                startActivity(intent)
            }
            binding.playButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                startActivity(intent)
            }
        } ?: run {
            binding.videoCard.visibility = View.GONE
        }
    }
}