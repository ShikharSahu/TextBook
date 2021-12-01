package com.example.socialsapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialsapp.daos.PostDao
import com.example.socialsapp.databinding.ActivityMainBinding
import com.example.socialsapp.databinding.FragmentHomeBinding
import com.example.socialsapp.databinding.FragmentProfileBinding
import com.example.socialsapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private val auth = Firebase.auth

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.ivProfileImage.context).load(auth.currentUser!!.photoUrl.toString()).circleCrop().into(binding.ivProfileImage)
        binding.tvProfileName.text = auth.currentUser!!.displayName



    }


}