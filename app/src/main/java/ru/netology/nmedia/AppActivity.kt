package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import ru.netology.nmedia.EditPostFragment.Companion.edit
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment_container).navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textArg = text
                }
            )

            findNavController(R.id.nav_host_fragment_container).navigate(
                R.id.action_feedFragment_to_editPostFragment,
                Bundle().apply {
                    edit = text
                }
            )
        }
        applyInset(findViewById(R.id.nav_host_fragment_container))
    }

    private fun applyInset(main: View) {
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Для клавиатуры:
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