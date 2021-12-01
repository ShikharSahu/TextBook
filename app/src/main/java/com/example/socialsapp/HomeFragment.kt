package com.example.socialsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.socialsapp.daos.PostDao
import com.example.socialsapp.databinding.FragmentHomeBinding
import com.example.socialsapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(), IOnClickForPostAdapter  {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postDao = PostDao()
        val postCollection = postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)

        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        adapter = PostAdapter(recyclerViewOptions, this )
        binding.rvFbPosts.adapter = adapter
        binding.rvFbPosts.layoutManager = LinearLayoutManager(context)


        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Toast.makeText(context, "on Move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val postId = adapter.snapshots.getSnapshot(viewHolder.adapterPosition).id
                val post = adapter.getItem(viewHolder.adapterPosition)
                if (postDao.isPostOfCurrentUser(post)){
                    postDao.deletePost(postId)
                    //Toast.makeText(context, "post Deleted ", Toast.LENGTH_SHORT).show()
                    val snackbar = Snackbar.make(binding.root, "post deleted",
                        Snackbar.LENGTH_LONG).setAction("UNDO") {
                        postDao.addPost(post)
                    }
                    snackbar.show()
                }
                else{
                    //Toast.makeText(context, "post not of user ", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                }
            }
        }

        val itemTouchHelper =  ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvFbPosts);


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
        //Toast.makeText(context,"add at pos:  $index", Toast.LENGTH_SHORT).show()
        binding.rvFbPosts.layoutManager!!.startSmoothScroll(smoothScroller)
    }

}