package com.neatroots.instagramclone.Post

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.neatroots.instagramclone.Models.Post
import com.neatroots.instagramclone.databinding.ActivityPostBinding
import com.neatroots.instagramclone.utils.POST
import com.neatroots.instagramclone.utils.POST_FOLDER
import com.neatroots.instagramclone.utils.uploadImage


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl : String ?= null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) {
                url->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.postButton.setOnClickListener{
            val post:Post = Post(imageUrl!!,binding.caption.editableText.toString())

            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                finish()
            }
        }
    }
}