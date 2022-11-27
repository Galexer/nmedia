package ru.netology.nmedia.activity

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmrntNewPostBinding
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

        val binding = FragmrntNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let(binding.content::setText)

        val text = binding.content.text.toString()
        if (text.isNotEmpty()) {
            binding.close.visibility = View.VISIBLE
            binding.close.setOnClickListener {
//                viewModel.changeContentAndSave(text)
                binding.close.visibility = View.GONE
                findNavController().navigateUp()
            }
        }

        binding.ok.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNotEmpty()) {
                viewModel.changeContentAndSave(content)
            }
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg by StringArg
    }
}