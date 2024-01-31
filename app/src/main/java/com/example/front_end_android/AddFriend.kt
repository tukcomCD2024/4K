package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.front_end_android.databinding.ActivityAddFriendBinding

class AddFriend : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        binding=ActivityAddFriendBinding.inflate(layoutInflater)

        binding.backFriendsImg.setOnClickListener {

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Replace with the instance of FriendsFragment
            val friendsFragment = FriendsFragment()
            fragmentTransaction.replace(R.id.navigationView, friendsFragment)

            // Add the transaction to the back stack to enable back navigation
            fragmentTransaction.addToBackStack(null)

            // Commit the transaction
            fragmentTransaction.commit()

        }
        binding.backFriendsTxt.setOnClickListener {

        }

    }
}