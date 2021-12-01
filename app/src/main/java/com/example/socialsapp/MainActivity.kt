package com.example.socialsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.example.socialsapp.databinding.ActivityMainBinding

import android.view.MenuItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this,CreatePost::class.java)
            startActivity(intent)
        }

        setFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.mi_home -> setFragment(homeFragment)
                R.id.mi_profile -> setFragment(profileFragment)
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(binding.mainActivityFrameLayout.id,
            fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_sign_out -> {
            MaterialAlertDialogBuilder(this,
                R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes") { dialog, which ->
                    GlobalScope.launch {
                        Firebase.auth.signOut()
                        withContext(Dispatchers.Main){
                            val signInIntent = Intent (baseContext, SignInActivity::class.java)
                            startActivity(signInIntent)
                            finish()
                        }
                    }

                }
                .setNegativeButton("No"){ _,_ -> }
                .show()
            }
        }
        return true
    }

}