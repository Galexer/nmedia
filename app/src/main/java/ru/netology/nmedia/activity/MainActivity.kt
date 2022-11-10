package ru.netology.nmedia

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import ru.netology.nmedia.adapter.OnInteractionsListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = PostAdapter(object : OnInteractionsListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.share(post.id)
            }

        })
        binding.posts.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.posts.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.content.requestFocus()
                AndroidUtils.showKeyboard(binding.content)
                binding.close.visibility = View.VISIBLE
                binding.content.setText(it.content)
            }
        }
        binding.save.setOnClickListener {
            with(binding.content) {
                val text = text.toString()
                if (text.isBlank()) {
                    Toast.makeText(this@MainActivity, R.string.empty_content, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text)
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(it)
                binding.close.visibility = View.GONE

            }
        }
        binding.close.setOnClickListener {
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(it)
            binding.close.visibility = View.GONE
        }
    }
}