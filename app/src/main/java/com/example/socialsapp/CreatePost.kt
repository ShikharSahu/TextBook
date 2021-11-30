package com.example.socialsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialsapp.daos.PostDao
import com.example.socialsapp.databinding.ActivityCreatePostBinding

class CreatePost : AppCompatActivity() {


    private lateinit var binding: ActivityCreatePostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreatePost.setOnClickListener{
            val text = binding.etUserPostIp.text.toString()
            if(text.isNotEmpty()){
                Toast.makeText(this,"text is $text",Toast.LENGTH_SHORT).show()

                val postDao = PostDao()
                postDao.addPost(text)
                finish()
            }
            else{
                Toast.makeText(this,"Post can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}