package com.example.socialsapp.daos

import com.example.socialsapp.models.Post
import com.example.socialsapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    private val auth = Firebase.auth

    fun addPost(text : String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(text,user,currentTime)
            postCollection.document().set(post)
        }
    }

    fun addPost(post : Post){
        GlobalScope.launch {
            postCollection.document().set(post)
        }
    }

    fun deletePost(postId: String ){
        GlobalScope.launch {
            postCollection.document(postId).delete()
        }
    }

    fun updateLikes(postId : String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val post = getPostById(postId).await().toObject(Post::class.java)
            val isLiked = post!!.likedBy.contains(currentUserId)
            if(isLiked){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)

        }
    }

    fun getPostById(postId : String) : Task<DocumentSnapshot> = postCollection.document(postId).get()

    fun isPostOfCurrentUser(post : Post) : Boolean {
        val currentUserId = auth.currentUser!!.uid
        return post.createdBy.uid == currentUserId
    }

}