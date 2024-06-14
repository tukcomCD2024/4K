package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.front_end_android.NaviActivity
import com.example.front_end_android.databinding.ActivityNaviBinding


private const val TAG_FRIENDS_LIST = "calender_fragment"
private const val TAG_RECENT_CALLS = "home_fragment"
private const val TAG_MY_INFO = "my_page_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setFragment(TAG_FRIENDS_LIST, FriendsFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.friendsFragment -> setFragment(TAG_FRIENDS_LIST, FriendsFragment())
                R.id.recentcallsFragment -> setFragment(TAG_RECENT_CALLS, RecentCallsFragment())
                R.id.myinfoFragment-> setFragment(TAG_MY_INFO, MyInfoFragment())
            }
            true
        }

    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val recentsCall = manager.findFragmentByTag(TAG_RECENT_CALLS)
        val friendsList = manager.findFragmentByTag(TAG_FRIENDS_LIST)
        val myInfo = manager.findFragmentByTag(TAG_MY_INFO)

        if (recentsCall != null){
            fragTransaction.hide(recentsCall)
        }

        if (friendsList != null){
            fragTransaction.hide(friendsList)
        }

        if (myInfo != null) {
            fragTransaction.hide(myInfo)
        }

        if (tag == TAG_FRIENDS_LIST) {
            if (friendsList!=null){
                fragTransaction.show(friendsList)
            }
        }
        else if (tag == TAG_RECENT_CALLS) {
            if (recentsCall != null) {
                fragTransaction.show(recentsCall)
            }
        }

        else if (tag == TAG_MY_INFO){
            if (myInfo != null){
                fragTransaction.show(myInfo)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}