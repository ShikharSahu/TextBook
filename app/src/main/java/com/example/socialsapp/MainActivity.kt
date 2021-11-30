package com.example.socialsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialsapp.daos.PostDao
import com.example.socialsapp.databinding.ActivityMainBinding
import com.example.socialsapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller


class MainActivity : AppCompatActivity(), IOnClickForPostAdapter {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    lateinit var smoothScroller: SmoothScroller


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this,CreatePost::class.java)
            startActivity(intent)
        }

        //options - > need a query to sort the data from the db
        postDao = PostDao()
        val postCollection = postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)

        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        adapter = PostAdapter(recyclerViewOptions, this)
        binding.rvFbPosts.adapter = adapter
        binding.rvFbPosts.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun scrollToNew(index: Int) {

        smoothScroller.targetPosition = index
        Toast.makeText(this,"add at pos:  $index", Toast.LENGTH_SHORT).show()
        binding.rvFbPosts.layoutManager!!.startSmoothScroll(smoothScroller)
    }
}