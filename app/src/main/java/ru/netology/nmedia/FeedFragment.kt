package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentFeedBinding.inflate(inflater,container, false)


        val adapter = PostAdapter(object : OnInteractionListener{
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

            override fun onEdit(post: Post) {
                viewModel.editContent(post)
                findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideoClick(videUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videUrl))
                startActivity(intent)
            }

        })

        binding.main.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }



        viewModel.edited.observe(viewLifecycleOwner) {post ->
            if (post.id == 0L) {
                return@observe
            }
        }

        binding.add.setOnClickListener{
            viewModel.editClear()
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

}