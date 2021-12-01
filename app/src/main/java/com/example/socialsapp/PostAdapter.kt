package com.example.socialsapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialsapp.databinding.PostRowItemBinding
import com.example.socialsapp.models.Post
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val IOnClickForPostAdapter : IOnClickForPostAdapter) :
    FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(val binding : PostRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val postViewHolder = PostViewHolder(PostRowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
        postViewHolder.binding.likeButton.setOnClickListener{
            IOnClickForPostAdapter.onLikeClicked(snapshots.getSnapshot(postViewHolder.adapterPosition).id)
        }

        return postViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.binding.userName.text = model.createdBy.displayName
        holder.binding.postTitle.text = model.text
        holder.binding.likeCount.text = "liked by ${model.likedBy.size}"
        Glide.with(holder.binding.userImage.context).load(model.createdBy.imgUrl).circleCrop().into(holder.binding.userImage)
        holder.binding.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid

        if(model.createdBy.uid == currentUserId){
            holder.binding.userName.text = "You"
        }
        else{
            holder.binding.userName.text = model.createdBy.displayName
        }
        Log.d("mee","${model.text}")

        if(model.likedBy.contains(currentUserId)){
            holder.binding.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeButton.context, R.drawable.ic_liked_24))
        }
        else{
            holder.binding.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeButton.context, R.drawable.ic_unliked_24))
        }
    }

    override fun onChildChanged(
        type: ChangeEventType,
        snapshot: DocumentSnapshot,
        newIndex: Int,
        oldIndex: Int
    ) {


        super.onChildChanged(type, snapshot, newIndex, oldIndex)
//        if(type == ChangeEventType.ADDED) {
//            IOnClickForPostAdapter.scrollToNew(newIndex)
//        }


    }


}

interface IOnClickForPostAdapter{
    fun onLikeClicked(postId : String)
    fun scrollToNew(index : Int)
}