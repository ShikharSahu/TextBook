package com.example.socialsapp.models

data class Post(
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0,
    val likedBy:ArrayList<String> = ArrayList()
)
