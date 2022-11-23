package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import ru.netology.nmedia.activity.NewPostActivity
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
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.share_post))
                startActivity(shareIntent)
            }

            override fun onVideo(post: Post) {
                val url = post.video
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

        })
        binding.posts.adapter = adapter

        val activityLauncher = registerForActivityResult(NewPostActivity.Contract) { text->
            if(text == null) {
                viewModel.edit(post= Post())
                return@registerForActivityResult
            }
            viewModel.changeContentAndSave(text)
        }

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
                activityLauncher.launch(it.content)
            }
            else {
                return@observe
            }
        }
        binding.add.setOnClickListener {
            activityLauncher.launch(null)
        }



//        binding.close.setOnClickListener {
//            viewModel.edit(post = Post())
//            binding.content.setText("")
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(it)
//            binding.close.visibility = View.GONE
//        }
    }
}