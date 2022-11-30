package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionsListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class OnePostFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val viewHolder = PostViewHolder(object : OnInteractionsListener {
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

            override fun onSee(post: Post) {
                findNavController().navigateUp()
            }

        }, binding.onePost)
        val id = arguments?.textArg?.toLong()

        viewModel.data.observe(viewLifecycleOwner) { posts->
            viewHolder.bind(posts.find { it.id == id } ?: run {
                findNavController().navigateUp()
                return@observe
            })
        }

        viewModel.edited.observe(viewLifecycleOwner) { post->
            if (post.id != 0L) {
                findNavController().navigate(R.id.action_onePostFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content }
                )
            }
            else {
                return@observe
            }
        }
        
        return binding.root
    }
}