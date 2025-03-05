package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg

    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.textArg?.let(binding.content::setText)

        binding.add.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(
                    activity,
                    this.getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                viewModel.changeContent(binding.content.text.toString())
                viewModel.saveContent()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }

        binding.cancelButton.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}